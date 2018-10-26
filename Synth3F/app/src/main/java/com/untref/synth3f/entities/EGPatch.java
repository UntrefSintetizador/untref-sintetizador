package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class EGPatch extends Patch {

    public float on_off = 1f;
    public float attack = 1f;
    public float decay = 1f;
    public float sustain = 1f;
    public float release = 0f;
    public float gate = 1f;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "attack", attack);
        processor.sendValue(name + "decay", decay);
        processor.sendValue(name + "sustain", sustain);
        processor.sendValue(name + "release", release);
        processor.sendValue(name + "gate", gate);
    }

    @Override
    public String getTypeName() {
        return "eg";
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    @Override
    public int getNumberOfOutputs() {
        return 1;
    }
}
