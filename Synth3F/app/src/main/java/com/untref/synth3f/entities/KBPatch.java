package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

public class KBPatch extends Patch {

    public float on_off = 1f;
    public float gate = 1f;
    public float midi_note = 0f;
    public float glide = 0f;
    public int octava = 4;

    @Override
    public void initialize(IProcessor processor) {
        String name = "x_" + getTypeName() + "_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "on-off", on_off);
        processor.sendValue(name + "gate", gate);
        processor.sendValue(name + "midi_note", midi_note);
        processor.sendValue(name + "glide", glide);
    }

    @Override
    public String getTypeName() {
        return "kb";
    }

    @Override
    public int getNumberOfInputs() {
        return 0;
    }

    @Override
    public int getNumberOfOutputs() {
        return 2;
    }
}
