package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PatchMenuView extends TableLayout {

    private static final int KNOBS_PER_ROW = 4;

    private PatchGraphFragment patchGraphFragment;
    private TextView parameterNameView;
    private EditText parameterValueView;
    private TextView patchMenuNameView;
    private PatchMenuCloseView patchMenuCloseView;
    private TableLayout parameterView;
    private OptionList optionList;
    private EnvelopeEditor envelopeEditor;
    private List<Knob> knobList;
    private PatchPresenter patchPresenter;
    private int knobSize;
    private int color;

    public PatchMenuView(Context context) {
        super(context);
    }

    public PatchMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public void setPatchGraphFragment(PatchGraphFragment patchGraphFragment) {
        this.patchGraphFragment = patchGraphFragment;
        knobList = new ArrayList<>();

        setVisibility(View.GONE);

        float relationOfHeight = setLayoutSize();
        int defaultKnobHeight = 140;
        knobSize = (int) (relationOfHeight * defaultKnobHeight);

        envelopeEditor = new EnvelopeEditor(getContext(), this,
                                            knobSize * KNOBS_PER_ROW, knobSize * 2);

        parameterView = findViewById(R.id.patch_menu_view_parameter);
        ((TableRow.LayoutParams) parameterView.getLayoutParams()).span = KNOBS_PER_ROW;

        initOptionList(relationOfHeight);
        initParameterNameView();
        initPatchMenuCloseView();
        initParameterValueView();
    }

    /**
     * Funcion para crear el knob. Los parametros van a ser atributos del knob a crear, y son necesarios
     * en el constructor del mismo.
     * Luego de crearlo, agrega al knob a una lista.
     */
    public void createKnob(Parameter parameter) {
        Knob newKnob = new Knob(getContext(), this, parameter, color);
        knobList.add(newKnob);
    }

    public void createOptionList(String parameterName, int[] iconOffIds, int[] iconOnIds,
                                 int selectedValue) {
        optionList.setValues(parameterName, color, iconOffIds, iconOnIds, selectedValue);
    }

    public void createEnvelopeEditor(Parameter attack, Parameter decay, Parameter sustain,
                                     Parameter release) {
        envelopeEditor.open(attack, decay, sustain, release, color);
    }

    /**
     * Vincula dos knobs para que el comportamiento de uno tenga influencia en el otro de acuerdo a
     * las funciones que se pasen como parametro
     *
     * @param parameterName1  es el nombre que representa al primer knob, el cual es atributo de cada
     *                        knob, y es dado cuando se construye.
     * @param parameterName2  es el nombre que representa al segundo knob, el cual es atributo de cada
     *                        knob, y es dado cuando se construye.
     * @param normalFunction  es la funcion que se ejecuta cuando se modifica un valor del primer knob
     *                        sobre el segundo knob, es decir su knob vinculado
     * @param inverseFunction es la funcion que se ejecuta cuando se modifica un valor del segundo knob
     *                        sobre el primer knob, es decir su knob vinculado. Esta funcion es la
     *                        inversa de la funcion normal
     */
    public void linkKnobs(String parameterName1, String parameterName2,
                          LinkingFunction normalFunction, LinkingFunction inverseFunction) {
        Knob firstKnob = null;
        Knob secondKnob = null;

        for (Knob knob : knobList) {
            String knobName = knob.getName();
            if (knobName.contentEquals(parameterName1)) {
                firstKnob = knob;
            } else if (knobName.contentEquals(parameterName2)) {
                secondKnob = knob;
            }
        }

        if (firstKnob != null && secondKnob != null) {
            firstKnob.link(secondKnob, parameterName2, inverseFunction);
            secondKnob.link(firstKnob, parameterName1, normalFunction);
        }
    }

    /**
     * Abre, es decir deja visible, la vista que permite editar y ver los valores del patch.
     *
     * @param patchPresenter presenter del patch
     * @param typeName nombre del tipo de patch
     */
    public void open(PatchPresenter patchPresenter, String typeName) {
        this.patchPresenter = patchPresenter;
        findViewById(R.id.patch_menu_title).setBackgroundColor(color);
        patchMenuCloseView.setBackgroundColor(color);
        patchMenuNameView.setText(typeName);
        patchMenuNameView.setBackgroundColor(color);
        styleParameterView();
        optionList.setVisibility(View.VISIBLE);
        showEnvelopeEditorIfOpen();
        populateKnobs();
        setVisibility(View.VISIBLE);
        if (!knobList.isEmpty()) {
            setParameterToEdit(knobList.get(0).getName(), knobList.get(0).getValue());
        } else if (envelopeEditor.isOpen()) {
            Parameter firstParameter = envelopeEditor.getFirstParameter();
            setParameterToEdit(firstParameter.getName(), firstParameter.getValue());
        }
    }

    /**
     * Vuelve a dejar No-Visible la vista para ver y editar el patch.
     */
    public void close() {
        knobList.clear();
        int permanentViews = 3;
        while (getChildCount() > permanentViews) {
            removeViewAt(permanentViews);
        }
        optionList.clear();
        optionList.setVisibility(View.GONE);
        if (envelopeEditor.isOpen()) {
            ((ViewGroup) envelopeEditor.getParent()).removeAllViews();
            envelopeEditor.close();
            envelopeEditor.setVisibility(View.GONE);
        }
        setVisibility(View.GONE);
    }

    public void setParameterToEdit(String parameterName, float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#######");
        parameterNameView.setText(parameterName);
        parameterValueView.setText(decimalFormat.format(value).replace(",", "."));
    }

    public void setValue(String parameterName, float value, boolean active) {
        if (active) {
            DecimalFormat decimalFormat = new DecimalFormat("#.#######");
            parameterValueView.setText(decimalFormat.format(value).replace(",",
                                                                           "."));
        }
        patchPresenter.setValue(parameterName, value);
    }

    private float setLayoutSize() {
        float defaultScreenHeight = 728;
        int defaultHeight = 680;
        int defaultWidth = 560;
        float relationOfHeight = (float) getResources().getDisplayMetrics().heightPixels /
                                 defaultScreenHeight;
        getLayoutParams().height = (int) (relationOfHeight * defaultHeight);
        getLayoutParams().width = (int) (relationOfHeight * defaultWidth);
        return relationOfHeight;
    }

    private void initOptionList(float relationOfHeight) {
        int defaultButtonSize = 80;
        int defaultButtonSpacing = 10;
        optionList = findViewById(R.id.patch_menu_view_option_list);
        optionList.setVisibility(View.GONE);
        optionList.init(this, KNOBS_PER_ROW, getLayoutParams().width,
                        (int) (relationOfHeight * defaultButtonSize), defaultButtonSpacing);
    }

    private void initParameterNameView() {
        parameterNameView = findViewById(R.id.patch_menu_view_parameter_name);
        patchMenuNameView = findViewById(R.id.patch_menu_name);
        ((TableRow.LayoutParams) patchMenuNameView.getLayoutParams()).span = KNOBS_PER_ROW - 1;
    }

    private void initPatchMenuCloseView() {
        patchMenuCloseView = findViewById(R.id.patch_menu_view_close);
        patchMenuCloseView.getLayoutParams().width = knobSize;
        patchMenuCloseView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        close();
                    }
                }
        );
    }

    private void initParameterValueView() {
        parameterValueView = findViewById(R.id.patch_menu_view_parameter_value);
        parameterValueView.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean enterPressed = event != null &&
                                               event.getAction() == KeyEvent.ACTION_DOWN &&
                                               event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                        if ((actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE || enterPressed)
                                && (event == null || !event.isShiftPressed())) {
                            changeValue(parameterValueView.getText());
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getContext().getSystemService(
                                            Context.INPUT_METHOD_SERVICE
                                    );
                            if (inputMethodManager != null) {
                                inputMethodManager.hideSoftInputFromWindow(
                                        parameterValueView.getWindowToken(), 0
                                );
                            }
                            return true;
                        }
                        return false;
                    }
                }
        );
    }

    private void changeValue(Editable editable) {
        Knob knob = null;
        for (Knob currentKnob : knobList) {
            if (currentKnob.getName().contentEquals(parameterNameView.getText())) {
                knob = currentKnob;
            }
        }

        float value = Float.parseFloat(editable.toString());
        if (knob != null) {
            value = knob.setValue(value);
        } else {
            value = Math.max(0, Math.min(optionList.getChildCount() - 1, Math.round(value)));
            optionList.selectValue((int) value);
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#######");
        editable.clear();
        editable.append(decimalFormat.format(value).replace(",", "."));
    }

    private void styleParameterView() {
        GradientDrawable parameterNameBackground = new GradientDrawable();
        parameterNameBackground.setColor(color);
        int cornerRadius = 15;
        parameterNameBackground.setCornerRadii(new float[] {
                cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0, 0, 0, 0
        });
        findViewById(R.id.patch_menu_view_parameter_name_row).setBackground(
                parameterNameBackground
        );

        parameterValueView.setTextColor(color);

        ((GradientDrawable) getBackground()).setStroke(2, color);
        GradientDrawable parameterBackground = new GradientDrawable();
        parameterBackground.setStroke(4, color);
        parameterBackground.setCornerRadius(cornerRadius);
        parameterView.setBackground(parameterBackground);
    }

    private void showEnvelopeEditorIfOpen() {
        if (envelopeEditor.isOpen()) {
            TableRow editorRow = new TableRow(getContext());
            editorRow.addView(envelopeEditor);
            addView(editorRow);
            TableRow.LayoutParams editorParams =
                    (TableRow.LayoutParams) envelopeEditor.getLayoutParams();
            editorParams.span = KNOBS_PER_ROW;
            editorParams.height = knobSize * 2;
            envelopeEditor.setVisibility(View.VISIBLE);
        }
    }

    private void populateKnobs() {
        for (int i = 0; i < knobList.size(); i = i + KNOBS_PER_ROW) {
            TableRow textTableRow = new TableRow(getContext());
            TableRow knobTableRow = new TableRow(getContext());
            int knobsInRow = Math.min(KNOBS_PER_ROW, knobList.size() - i);
            for (int j = 0; j < knobsInRow; j++) {
                Knob knob = knobList.get(i + j);
                TextView textView = new TextView(getContext());
                textView.setText(knob.getName());
                textView.setTextAppearance(getContext(), R.style.PatchMenuText);
                textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                textTableRow.addView(textView);
                knobTableRow.addView(knob);
                LinearLayout.LayoutParams layoutParams =
                        (LinearLayout.LayoutParams) knob.getLayoutParams();
                layoutParams.width = knobSize;
                layoutParams.height = knobSize;
            }
            addView(knobTableRow);
            addView(textTableRow);
        }
        fixRowsWidth();
    }

    private void fixRowsWidth() {
        if (knobList.isEmpty()) {
            TableRow dummyRow = new TableRow(getContext());
            addView(dummyRow);
        }
        ViewGroup lastRow = (ViewGroup) getChildAt(getChildCount() - 1);
        while (lastRow.getChildCount() < KNOBS_PER_ROW - 1) {
            View dummy = new View(getContext());
            lastRow.addView(dummy);
            dummy.getLayoutParams().width = knobSize;
        }
    }
}
