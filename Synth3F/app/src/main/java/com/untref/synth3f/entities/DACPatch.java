package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;

public class DACPatch extends Patch {
    public float on_off = 1f;

    @Override
    public void initialize(BaseProcessor processor) {
        String name = "x_dac_" + Integer.toString(getId()) + "_";
    }
}
