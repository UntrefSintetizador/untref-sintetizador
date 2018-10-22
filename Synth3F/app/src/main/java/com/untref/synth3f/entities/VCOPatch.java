package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class VCOPatch extends Patch {

    public float on_off = 1f;
    public float att_freq0 = 100f;
    public float att_freq1 = 100f;
    public float att_pw = 100f;
    public float shape = 0f;
    public float freq = 261.626f;
    public float offset = 0f;
    public float pw = 50;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "att_freq0", att_freq0);
        processor.sendValue(name + "att_freq1", att_freq1);
        processor.sendValue(name + "att_pw", att_pw);
        processor.sendValue(name + "shape", shape);
        processor.sendValue(name + "freq", freq);
        processor.sendValue(name + "offset", offset);
        processor.sendValue(name + "pw", pw);
    }

    @Override
    public String getTypeName() {
        return "vco";
    }

    @Override
    public int getNumberOfInputs() {
        return 3;
    }

    @Override
    public int getNumberOfOutputs() {
        return 1;
    }
}
