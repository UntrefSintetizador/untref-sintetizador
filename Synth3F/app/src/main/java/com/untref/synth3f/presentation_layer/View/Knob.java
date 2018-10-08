package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.untref.synth3f.R;

import java.math.BigDecimal;

public class Knob extends AppCompatImageView implements View.OnTouchListener {

    private static final int MAX_ROTATION = 135;

    private PatchMenuView2 patchMenuView2;

    private String parameterName;
    private float maxValue;
    private float minValue;
    private int precision;
    private float value;
    private PatchMenuView.MenuScale scale;

    private int rotation;
    private int initialTouch;
    private Knob linkedKnob;
    private String linkedKnobParameterName;

    public Knob(Context context) {
        super(context);
    }

    public Knob(Context context, PatchMenuView2 patchMenuView2, String parameterName,
                float maxValue, float minValue, int precision, float value,
                PatchMenuView.MenuScale scale, ColorStateList colorStateList) {
        super(context);
        this.patchMenuView2 = patchMenuView2;
        this.parameterName = parameterName;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.precision = precision;
        this.value = value;
        this.scale = scale;
        init(colorStateList);
    }

    public String getName() {
        return parameterName;
    }

    public float getValue() {
        return value;
    }

    public int calculateRotation() {
        double percentage = 0f;
        if (scale == PatchMenuView.MenuScale.linear) {
            percentage = (value - minValue) / (maxValue - minValue);
        } else if (scale == PatchMenuView.MenuScale.exponential_left) {
            percentage = Math.log(value - minValue + 1) / Math.log(maxValue + 1);
        } else if (scale == PatchMenuView.MenuScale.exponential_center) {
            double aux;
            if (value > 0) {
                aux = Math.log(value + 1) / Math.log(maxValue + 1);
            } else {
                aux = -Math.log(-value + 1) / Math.log(-minValue + 1);
            }
            percentage = (aux + 1) / 2;
        }
        return (int) (percentage * MAX_ROTATION * 2) - MAX_ROTATION;
    }

    public void changeValue(int rotation) {
        double percentage = (double) (rotation + MAX_ROTATION) / (MAX_ROTATION * 2);
        if (scale == PatchMenuView.MenuScale.linear) {
            value = (float) (minValue + (percentage * (maxValue - minValue)));
        } else if (scale == PatchMenuView.MenuScale.exponential_left) {
            value = (float) (minValue + Math.pow(maxValue + 1, percentage) - 1);
        } else if (scale == PatchMenuView.MenuScale.exponential_center) {
            if (percentage > 0.5) {
                value = (float) Math.pow(maxValue + 1, (percentage - 0.5) * 2) - 1;
            } else {
                value = (float) -Math.pow(-minValue + 1, -(percentage - 0.5) * 2) + 1;
            }
        }
        normalizeValue();
        setRotation(calculateRotation());
    }

    public float setValue(String strValue) {
        value = Float.parseFloat(strValue);
        value = Math.max(minValue, Math.min(maxValue, value));
        normalizeValue();
        rotation = calculateRotation();
        setRotation(rotation);
        return value;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int y = (int) motionEvent.getRawY();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                initialTouch = y;
                patchMenuView2.setParameterToEdit(parameterName, value);
                break;
            case MotionEvent.ACTION_MOVE:
                int clampedRotation = Math.max(-MAX_ROTATION, Math.min(MAX_ROTATION, rotation + y - initialTouch));
                changeValue(-clampedRotation);
                patchMenuView2.setValue(parameterName, value);

                if (linkedKnob != null) {
                    linkedKnob.changeValue(-clampedRotation);
                    patchMenuView2.setValue(linkedKnobParameterName, value);
                }

                break;
            case MotionEvent.ACTION_UP:
                rotation = Math.max(-MAX_ROTATION, Math.min(MAX_ROTATION, rotation + y - initialTouch));
                break;
            default:
                break;
        }
        return true;
    }

    private void init(ColorStateList colorStateList) {
        rotation = calculateRotation();
        setRotation(rotation);
        setClickable(true);
        setOnTouchListener(this);
        setBackgroundResource(R.drawable.knob);
        setScaleType(AppCompatImageView.ScaleType.CENTER_INSIDE);
        setSupportBackgroundTintList(colorStateList);
        setSupportBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
    }

    private void normalizeValue() {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP);
        value = bigDecimal.floatValue();
    }

    public void link(Knob knob, String parameterName) {
        linkedKnob = knob;
        linkedKnobParameterName = parameterName;
    }
}
