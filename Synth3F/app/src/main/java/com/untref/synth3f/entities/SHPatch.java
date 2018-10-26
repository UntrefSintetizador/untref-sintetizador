package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class SHPatch extends Patch {

    public float on_off = 1f;
    public float att_signal = 100f;
    public float glide = 0f;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "att_signal", att_signal);
        processor.sendValue(name + "glide", glide);
    }

    @Override
    public String getTypeName() {
        return "sh";
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
