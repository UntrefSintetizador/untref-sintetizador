package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private OptionList optionList;
    private PatchPresenter patchPresenter;
    private List<Knob> knobList;
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
        this.optionList = ((View) getParent()).findViewById(R.id.patch_menu_view_option_list);

        setBackgroundColor(Color.MAGENTA);
        optionList.setVisibility(View.GONE);
        setVisibility(View.GONE);

        this.knobsPerRow = 4;
        ((TableRow.LayoutParams) parameterNameView.getLayoutParams()).span = knobsPerRow - 1;
        ((TableRow.LayoutParams) parameterValueView.getLayoutParams()).span = knobsPerRow;

        //pixels
        float defaultScreenHeight = 728;
        int defaultHeight = 600;
        int defaultWidth = 600;
        int defaultKnobHeight = 150;
        float relationOfHeight = (float) getResources().getDisplayMetrics().heightPixels / defaultScreenHeight;
        getLayoutParams().height = (int) (relationOfHeight * defaultHeight);
        getLayoutParams().width = (int) (relationOfHeight * defaultWidth);
        this.knobSize = (int) (relationOfHeight * defaultKnobHeight);

        int defaultButtonSize = 150;
        optionList.init(this, (int) (relationOfHeight * defaultButtonSize));

        int defaultCloseButtonSize = 50;
        Button button = findViewById(R.id.patch_menu_view_close);
        button.getLayoutParams().width = (int) (relationOfHeight * defaultCloseButtonSize);
        button.getLayoutParams().height = (int) (relationOfHeight * defaultCloseButtonSize);
        button.setOnClickListener(
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
                scale.getMinValue(), scale.getMaxValue(), precision, value, scale, color);

        knobList.add(newKnob);
    }

    public void createOptionList(String parameterName, int[] imageIds, int selectedValue) {
        optionList.setValues(parameterName, imageIds, selectedValue, color);
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
     */
    public void open(PatchPresenter patchPresenter) {
        this.patchPresenter = patchPresenter;
        TableRow knobTableRow;
        TableRow textTableRow;
        int knobsInRow;
        TextView textView;
        Knob knob;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < knobList.size(); i = i + knobsPerRow) {
            textTableRow = new TableRow(getContext());
            knobTableRow = new TableRow(getContext());
            knobsInRow = Math.min(knobsPerRow, knobList.size() - i);
            for (int j = 0; j < knobsInRow; j++) {
                knob = knobList.get(i + j);
                textView = new TextView(getContext());
                textView.setText(knob.getName());
                textTableRow.addView(textView);
                knobTableRow.addView(knob);
                layoutParams = (LinearLayout.LayoutParams) knob.getLayoutParams();
                layoutParams.width = knobSize;
                layoutParams.height = knobSize;
            }
            addView(textTableRow);
            addView(knobTableRow);
        }
        optionList.setVisibility(View.VISIBLE);
        setVisibility(View.VISIBLE);
        setParameterToEdit(knobList.get(0).getName(), knobList.get(0).getValue());
    }

    /**
     * Vuelve a dejar No-Visible la vista para ver y editar el patch.
     */
    public void close() {
        knobList.clear();
        int permanentViews = 2;
        while (getChildCount() > permanentViews) {
            removeViewAt(permanentViews);
        }
        optionList.clear();
        optionList.setVisibility(View.GONE);
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

    public void changeValue(Editable editable) {
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
}
