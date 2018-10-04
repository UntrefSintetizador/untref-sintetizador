package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
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

public class PatchMenuView2 extends TableLayout {

    private PatchGraphFragment patchGraphFragment;
    private TextView parameterNameView;
    private EditText parameterValueView;
    private OptionList optionList;
    private PatchPresenter patchPresenter;
    private ColorStateList colorStateList;
    private List<Knob> knobList;
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

        //test
        this.colorStateList = getContext().getResources().getColorStateList(R.color.blue_soft);
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public void createKnob(String parameterName, float maxValue, float minValue,
                           int precision, float value, PatchMenuView.MenuScale scale) {
        knobList.add(new Knob(getContext(), this, parameterName, maxValue, minValue,
                precision, value, scale, colorStateList));
    }

    public void createOptionList(String parameterName, int[] imageIds, int selectedValue) {
        optionList.setValues(parameterName, imageIds, selectedValue);
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
        parameterValueView.setText(decimalFormat.format(value));
    }

    public void setValue(String parameterName, float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#######");
        parameterValueView.setText(decimalFormat.format(value));
        patchPresenter.setValue(parameterName, value);
    }
}
