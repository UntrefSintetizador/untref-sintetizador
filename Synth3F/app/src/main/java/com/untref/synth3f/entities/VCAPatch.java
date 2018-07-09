package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;

public class VCAPatch extends Patch {
    public float on_off = 1f;
    public float att_control = 1f;
    public float base = 0f;
    public float clip = 0f;

    @Override
    public void initialize(BaseProcessor processor) {
        String name = "x_vca_" + Integer.toString(getId()) + "_";
        processor.sendValue(name + "att_control", att_control);
        processor.sendValue(name + "base", base);
        processor.sendValue(name + "clip", clip);
    }
}
