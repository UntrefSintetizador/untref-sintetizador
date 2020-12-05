package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

public class VCFPatch extends Patch {

    public float on_off = 1f;
    public float att_signal = 100f;
    public float att_freq = 100f;
    public float mode = 0f;
    public float freq = 261.626f;
    public float q = 1f;

    @Override
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_att_signal), att_signal);
        processor.sendValue(name + resources.getString(R.string.parameter_att_freq), att_freq);
        processor.sendValue(name + resources.getString(R.string.parameter_mode), mode);
        processor.sendValue(name + resources.getString(R.string.parameter_freq), freq);
        processor.sendValue(name + resources.getString(R.string.parameter_q), q);
    }

    @Override
    public String getTypeName() {
        return "vcf";
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
