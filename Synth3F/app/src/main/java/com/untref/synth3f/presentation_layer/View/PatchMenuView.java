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
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PatchMenuView extends TableLayout {

    private PatchGraphFragment patchGraphFragment;
    private TextView parameterNameView;
    private EditText parameterValueView;
    private TextView patchMenuViewName;
    private PatchMenuCloseView patchMenuViewClose;
    private TableLayout parameterView;
    private OptionList optionList;
    private EnvelopeEditor envelopeEditor;
    private List<Knob> knobList;
    private PatchPresenter patchPresenter;
    private int knobSize;
    private int knobsPerRow;
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
        this.knobList = new ArrayList<>();
        this.parameterNameView = findViewById(R.id.patch_menu_view_parameter_name);
        this.parameterValueView = findViewById(R.id.patch_menu_view_parameter_value);
        this.optionList = findViewById(R.id.patch_menu_view_option_list);
        this.envelopeEditor = new EnvelopeEditor(getContext());

        this.knobsPerRow = 4;

        optionList.setVisibility(View.GONE);
        setVisibility(View.GONE);

        //pixels
        float defaultScreenHeight = 728;
        int defaultHeight = 680;
        int defaultWidth = 560;
        int defaultKnobHeight = 140;
        float relationOfHeight = (float) getResources().getDisplayMetrics().heightPixels / defaultScreenHeight;
        getLayoutParams().height = (int) (relationOfHeight * defaultHeight);
        getLayoutParams().width = (int) (relationOfHeight * defaultWidth);
        this.knobSize = (int) (relationOfHeight * defaultKnobHeight);

        parameterView = findViewById(R.id.patch_menu_view_parameter);
        ((TableRow.LayoutParams) parameterView.getLayoutParams()).span = knobsPerRow;

        int defaultButtonSize = 80;
        int defaultButtonSpacing = 10;
        optionList.init(this, knobsPerRow, getLayoutParams().width,
                        (int) (relationOfHeight * defaultButtonSize), defaultButtonSpacing);

        patchMenuViewName = findViewById(R.id.patch_menu_name);
        ((TableRow.LayoutParams) patchMenuViewName.getLayoutParams()).span = knobsPerRow - 1;

        patchMenuViewClose = findViewById(R.id.patch_menu_view_close);
        patchMenuViewClose.getLayoutParams().width = knobSize;
        patchMenuViewClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        close();
                    }
                }
        );

        parameterValueView.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                                && (event == null || !event.isShiftPressed())) {
                            changeValue(parameterValueView.getText());
                            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null) {
                                inputMethodManager.hideSoftInputFromWindow(parameterValueView.getWindowToken(), 0);
                            }
                            return true;
                        }
                        return false;
                    }
                }
        );
    }

    /**
     * Funcion para crear el knob. Los parametros van a ser atributos del knob a crear, y son necesarios
     * en el constructor del mismo.
     * Luego de crearlo, agrega al knob a una lista.
     */
    public void createKnob(String parameterName, int precision, float value,
                           MenuScaleFunction scale) {

        Knob newKnob = new Knob(getContext(), this, parameterName,
                                scale.getMinValue(), scale.getMaxValue(), precision, value,
                                scale, color);

        knobList.add(newKnob);
    }

    // Remove color parameter
    public void createOptionList(String parameterName, int color, int[] iconOffIds,
                                 int[] iconOnIds, int selectedValue) {
        optionList.setValues(parameterName, color, iconOffIds, iconOnIds, selectedValue);
    }

    public void createEnvelopeEditor() {
        envelopeEditor.open(knobSize * knobsPerRow, knobSize * 2, color);
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
    public void linkKnobs(String parameterName1, String parameterName2, LinkingFunction normalFunction, LinkingFunction inverseFunction) {
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
        patchMenuViewClose.setBackgroundColor(color);
        patchMenuViewName.setText(typeName);
        patchMenuViewName.setBackgroundColor(color);
        styleParameterView();
        optionList.setVisibility(View.VISIBLE);
        if (envelopeEditor.isOpen()) {
            TableRow editorRow = new TableRow(getContext());
            editorRow.addView(envelopeEditor);
            editorRow.setBackgroundColor(0);
            addView(editorRow);
            TableRow.LayoutParams editorParams =
                    (TableRow.LayoutParams) envelopeEditor.getLayoutParams();
            editorParams.span = knobsPerRow;
            editorParams.height = knobSize * 2;
            envelopeEditor.setVisibility(View.VISIBLE);
        }
        populateKnobs();
        setVisibility(View.VISIBLE);
        // TEMP
        if (!knobList.isEmpty()) {
            setParameterToEdit(knobList.get(0).getName(), knobList.get(0).getValue());
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
            parameterValueView.setText(decimalFormat.format(value).replace(",", "."));
        }
        patchPresenter.setValue(parameterName, value);
    }

    private void changeValue(Editable editable) {
        Knob knob = null;
        float value;

        for (Knob currentKnob : knobList) {

            if (currentKnob.getName().contentEquals(parameterNameView.getText())) {
                knob = currentKnob;
            }
        }

        if (knob != null) {
            value = Float.parseFloat(editable.toString());
            value = knob.setValue(value);

        } else {
            value = Float.parseFloat(editable.toString());
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
        findViewById(R.id.patch_menu_view_parameter_name_row).setBackground(parameterNameBackground);
        parameterValueView.setTextColor(color);
        ((GradientDrawable) getBackground()).setStroke(2, color);
        GradientDrawable parameterBackground = new GradientDrawable();
        parameterBackground.setStroke(4, color);
        parameterBackground.setCornerRadius(cornerRadius);
        parameterView.setBackground(parameterBackground);
    }

    private void populateKnobs() {
        TableRow knobTableRow;
        TableRow textTableRow;
        int knobsInRow;
        TextView textView;
        Knob knob;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < knobList.size(); i = i + knobsPerRow) {
            textTableRow = new TableRow(getContext());
            knobTableRow = new TableRow(getContext());
            textTableRow.setBackgroundColor(0);
            knobTableRow.setBackgroundColor(0);
            knobsInRow = Math.min(knobsPerRow, knobList.size() - i);
            for (int j = 0; j < knobsInRow; j++) {
                knob = knobList.get(i + j);
                textView = new TextView(getContext());
                textView.setBackgroundColor(0);
                textView.setText(knob.getName());
                textView.setTextAppearance(getContext(), R.style.PatchMenuText);
                textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                textTableRow.addView(textView);
                knobTableRow.addView(knob);
                layoutParams = (LinearLayout.LayoutParams) knob.getLayoutParams();
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
            dummyRow.setBackgroundColor(0);
            addView(dummyRow);
        }
        ViewGroup lastRow = (ViewGroup) getChildAt(getChildCount() - 1);
        while (lastRow.getChildCount() < knobsPerRow - 1) {
            View dummy = new View(getContext());
            lastRow.addView(dummy);
            dummy.getLayoutParams().width = knobSize;
        }
    }
}
