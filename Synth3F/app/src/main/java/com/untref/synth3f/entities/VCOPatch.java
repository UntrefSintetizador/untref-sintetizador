package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

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
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_att_freq0), att_freq0);
        processor.sendValue(name + resources.getString(R.string.parameter_att_freq1), att_freq1);
        processor.sendValue(name + resources.getString(R.string.parameter_att_pw), att_pw);
        processor.sendValue(name + resources.getString(R.string.parameter_shape), shape);
        processor.sendValue(name + resources.getString(R.string.parameter_freq), freq);
        processor.sendValue(name + resources.getString(R.string.parameter_offset), offset);
        processor.sendValue(name + resources.getString(R.string.parameter_pw), pw);
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
