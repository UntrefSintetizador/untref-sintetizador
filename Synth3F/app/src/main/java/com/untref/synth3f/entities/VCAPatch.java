package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class VCAPatch extends Patch {

    public float on_off = 1f;
    public float att_control = 100f;
    public float base = 1f;
    public float clip = 0f;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "att_control", att_control);
        processor.sendValue(name + "base", base);
        processor.sendValue(name + "clip", clip);
    }

    @Override
    public String getTypeName() {
        return "vca";
    }

    @Override
    public int getNumberOfInputs() {
        return 2;
    }

    @Override
    public int getNumberOfOutputs() {
        return 1;
    }
}
