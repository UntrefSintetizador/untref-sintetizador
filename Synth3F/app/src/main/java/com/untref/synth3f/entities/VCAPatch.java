package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

public class VCAPatch extends Patch {

    public float on_off = 1f;
    public float att_control = 100f;
    public float base = 1f;
    public float clip = 0f;

    @Override
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_att_control), att_control);
        processor.sendValue(name + resources.getString(R.string.parameter_base), base);
        processor.sendValue(name + resources.getString(R.string.parameter_clip), clip);
    }

    @Override
    public String getTypeName() {
        return "vca";
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
