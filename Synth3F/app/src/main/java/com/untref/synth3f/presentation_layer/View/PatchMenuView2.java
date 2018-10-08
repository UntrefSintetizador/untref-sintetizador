package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.content.res.ColorStateList;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatchMenuView2 extends TableLayout {

    private PatchGraphFragment patchGraphFragment;
    private TextView parameterNameView;
    private EditText parameterValueView;
    private OptionList optionList;
    private PatchPresenter patchPresenter;
    private ColorStateList colorStateList;
    private List<Knob> knobList;
    private Map<String, Knob> knobMap;
    private int knobSize;
    private int knobsPerRow;

    public PatchMenuView2(Context context) {
        super(context);
    }

    public PatchMenuView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPatchGraphFragment(PatchGraphFragment patchGraphFragment) {
        this.patchGraphFragment = patchGraphFragment;
        this.knobList = new ArrayList<>();
        this.knobMap = new HashMap<>();
        this.parameterNameView = (TextView) findViewById(R.id.patch_menu_view_parameter_name);
        this.parameterValueView = (EditText) findViewById(R.id.patch_menu_view_parameter_value);
        this.optionList = (OptionList) ((View) getParent()).findViewById(R.id.patch_menu_view_option_list);

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
        optionList.init(this, (int) (relationOfHeight * defaultButtonSize), R.color.colorPrimary, R.color.colorPrimaryDark);

        int defaultCloseButtonSize = 50;
        Button button = (Button) findViewById(R.id.patch_menu_view_close);
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

        //test
        this.colorStateList = getContext().getResources().getColorStateList(R.color.blue_soft);
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public void createKnob(String parameterName, float maxValue, float minValue,
                           int precision, float value, PatchMenuView.MenuScale scale) {

        Knob newKnob = new Knob(getContext(), this, parameterName, maxValue,
                minValue, precision, value, scale, colorStateList);

        knobList.add(newKnob);
        knobMap.put(parameterName, newKnob);
    }

    public void createOptionList(String parameterName, int[] imageIds, int selectedValue) {
        optionList.setValues(parameterName, imageIds, selectedValue);
    }

    public void linkKnobs(String parameterName1, String parameterName2) {
        Knob firstKnob = knobMap.get(parameterName1);
        Knob secondKnob = knobMap.get(parameterName2);

        firstKnob.link(secondKnob, parameterName2);
        secondKnob.link(firstKnob, parameterName1);
    }

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

    public void setValue(String parameterName, float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#######");
        parameterValueView.setText(decimalFormat.format(value).replace(",", "."));
        patchPresenter.setValue(parameterName, value);
    }

    public void changeValue(Editable editable) {
        Knob knob = null;
        float value;
        for (int i = 0; i < knobList.size(); i++) {
            if (knobList.get(i).getName().contentEquals(parameterNameView.getText())) {
                knob = knobList.get(i);
            }
        }
        if(knob != null){
            value = knob.setValue(editable.toString());
            patchPresenter.setValue(knob.getName(), value);
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
