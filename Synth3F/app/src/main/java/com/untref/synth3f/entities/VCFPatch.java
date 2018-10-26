package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class VCFPatch extends Patch {

    public float on_off = 1f;
    public float att_signal = 100f;
    public float att_freq = 100f;
    public float mode = 0f;
    public float freq = 261.626f;
    public float q = 1f;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "att_signal", att_signal);
        processor.sendValue(name + "att_freq", att_freq);
        processor.sendValue(name + "mode", mode);
        processor.sendValue(name + "freq", freq);
        processor.sendValue(name + "q", q);
    }

    @Override
    public String getTypeName() {
        return "vcf";
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
