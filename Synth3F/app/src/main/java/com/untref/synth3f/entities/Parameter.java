package com.untref.synth3f.entities;

import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;

public class Parameter {
    private String name;
    private float value;
    private int precision;
    private MenuScaleFunction scaleFunction;

    public Parameter(String name, float value, int precision, MenuScaleFunction scaleFunction) {
        this.name = name;
        this.value = value;
        this.precision = precision;
        this.scaleFunction = scaleFunction;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getPrecision() {
        return precision;
    }

    public MenuScaleFunction getScaleFunction() {
        return scaleFunction;
    }
}
