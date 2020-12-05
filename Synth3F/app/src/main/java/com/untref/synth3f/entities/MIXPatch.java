package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

public class MIXPatch extends Patch {

    public float on_off = 1f;
    public float ch1 = 0.5f;
    public float ch2 = 0.5f;
    public float ch3 = 0.5f;
    public float ch4 = 0.5f;
    public float master = 1f;

    @Override
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_ch1), ch1);
        processor.sendValue(name + resources.getString(R.string.parameter_ch2), ch2);
        processor.sendValue(name + resources.getString(R.string.parameter_ch3), ch3);
        processor.sendValue(name + resources.getString(R.string.parameter_ch4), ch4);
        processor.sendValue(name + resources.getString(R.string.parameter_master), master);
    }

    @Override
    public String getTypeName() {
        return "mix";
    }

    @Override
    public int getNumberOfInputs() {
        return 4;
    }

    @Override
    public int getNumberOfOutputs() {
        return 1;
    }
}
