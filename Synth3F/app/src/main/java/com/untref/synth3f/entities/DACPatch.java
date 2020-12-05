package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.Processor;

public class DACPatch extends Patch {

    public float on_off = 1f;

    @Override
    public void initialize(Processor processor) {
        String name = "x_" + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + "on-off", on_off);
    }

    @Override
    public String getTypeName() {
        return "dac";
    }

    @Override
    public int getNumberOfInputs() {
        return 2;
    }

    @Override
    public int getNumberOfOutputs() {
        return 0;
    }
}
