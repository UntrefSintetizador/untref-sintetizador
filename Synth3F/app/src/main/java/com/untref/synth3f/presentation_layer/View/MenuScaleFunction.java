package com.untref.synth3f.presentation_layer.View;

public abstract class MenuScaleFunction {

    private final float minValue;
    private final float maxValue;

    public MenuScaleFunction(float minValue, float maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public abstract float calculate(float x);

    public abstract float calculateInverse(float x);
}
