package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

public class SHPatch extends Patch {

    public float on_off = 1f;
    public float att_signal = 100f;
    public float glide = 0f;

    @Override
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_att_signal), att_signal);
        processor.sendValue(name + resources.getString(R.string.parameter_glide), glide);
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
