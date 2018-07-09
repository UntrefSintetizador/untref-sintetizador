package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;

public class EGPatch extends Patch {
    public float on_off = 1f;
    public float attack = 1f;
    public float decay = 1f;
    public float sustain = 1f;
    public float release = 0f;
    public float gate = 261.626f;

    @Override
    public void initialize(BaseProcessor processor) {
        String name = "x_eg_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "attack", attack);
        processor.sendValue(name + "decay", decay);
        processor.sendValue(name + "sustain", sustain);
        processor.sendValue(name + "release", release);
        processor.sendValue(name + "gate", gate);
    }
}
