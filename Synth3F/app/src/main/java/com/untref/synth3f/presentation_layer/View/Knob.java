package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.untref.synth3f.R;

import java.math.BigDecimal;

public class Knob extends AppCompatImageView implements View.OnTouchListener {

    private static final int MAX_ROTATION = 135;

    private PatchMenuView patchMenuView;

    private String parameterName;
    private float maxValue;
    private float minValue;
    private int precision;
    private float value;
    private MenuScaleFunction scaleFunction;

    private int rotationWhileNotMoving;
    private int initialTouch;
    private Knob linkedKnob;
    private String linkedKnobParameterName;
    private LinkingFunction linkingFunction;

    public Knob(Context context) {
        super(context);
    }

    public Knob(Context context, PatchMenuView patchMenuView, String parameterName,
                float minValue, float maxValue, int precision, float value,
                MenuScaleFunction scaleFunction, int color) {
        super(context);
        this.patchMenuView = patchMenuView;
        this.parameterName = parameterName;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.precision = precision;
        this.value = value;
        this.scaleFunction = scaleFunction;
        init(color);
    }

    public String getName() {
        return parameterName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        setValueCheckingLinks(value, true);
    }

    public void link(Knob knob, String parameterName, LinkingFunction linkingFunction) {
        linkedKnob = knob;
        linkedKnobParameterName = parameterName;
        this.linkingFunction = linkingFunction;
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
                patchMenuView.setParameterToEdit(parameterName, value);
                break;

            case MotionEvent.ACTION_MOVE:
                int clampedRotation = getClampedRotation(rotationWhileNotMoving, y, initialTouch);
                convertRotationToValue(clampedRotation);
                setRotation(calculateRotation());
                checkLinkedKnob();
                patchMenuView.setValue(parameterName, value);
                break;

            case MotionEvent.ACTION_UP:
                rotationWhileNotMoving = getClampedRotation(rotationWhileNotMoving, y, initialTouch);
                break;

            default:
                break;
        }

        return true;
    }

    private void init(int color) {
        rotationWhileNotMoving = calculateRotation();
        setRotation(rotationWhileNotMoving);
        setClickable(true);
        setOnTouchListener(this);
        setBackgroundResource(R.drawable.knob);
        setScaleType(AppCompatImageView.ScaleType.CENTER_INSIDE);
        getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    private void setValueCheckingLinks(float value, boolean checksLink) {
        this.value = Math.max(minValue, Math.min(maxValue, value));
        normalizeValue();
        rotationWhileNotMoving = calculateRotation();
        setRotation(rotationWhileNotMoving);

        if (checksLink) {
            checkLinkedKnob();
        }

        patchMenuView.setValue(getName(), value);
    }

    private void normalizeValue() {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP);
        value = bigDecimal.floatValue();
    }

    private void checkLinkedKnob() {

        if (linkedKnob != null) {
            linkedKnob.setValueCheckingLinks(linkingFunction.calculate(value), false);
            patchMenuView.setValue(linkedKnobParameterName, linkedKnob.getValue());
        }
    }

    private int getClampedRotation(int rotation, int y, int initialTouch) {
        return Math.max(-MAX_ROTATION, Math.min(MAX_ROTATION, rotation - y + initialTouch));
    }

    private int calculateRotation() {
        float percentage = scaleFunction.calculateInverse(value);

        return (int) (percentage * MAX_ROTATION * 2) - MAX_ROTATION;
    }

    private void convertRotationToValue(int rotation) {
        float percentage = (float) (rotation + MAX_ROTATION) / (MAX_ROTATION * 2);
        value = scaleFunction.calculate(percentage);
        normalizeValue();
    }
}
