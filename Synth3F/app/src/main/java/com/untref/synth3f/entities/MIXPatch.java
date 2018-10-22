package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class MIXPatch extends Patch {

    public float on_off = 1f;
    public float ch1 = 0.5f;
    public float ch2 = 0.5f;
    public float ch3 = 0.5f;
    public float ch4 = 0.5f;
    public float master = 1f;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "ch1", ch1);
        processor.sendValue(name + "ch2", ch2);
        processor.sendValue(name + "ch3", ch3);
        processor.sendValue(name + "ch4", ch4);
        processor.sendValue(name + "master", master);
    }

    @Override
    public String getTypeName() {
        return "mix";
    }

    @Override
    public int getNumberOfInputs() {
        return 4;
    }

    @Override
    public int getNumberOfOutputs() {
        return 1;
    }
}
