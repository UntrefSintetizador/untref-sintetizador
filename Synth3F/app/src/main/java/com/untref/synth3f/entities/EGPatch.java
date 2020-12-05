package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

public class EGPatch extends Patch {

    public float on_off = 1f;
    public float attack = 1f;
    public float decay = 1f;
    public float sustain = 1f;
    public float release = 0f;
    public float gate = 1f;

    @Override
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_attack), attack);
        processor.sendValue(name + resources.getString(R.string.parameter_decay), decay);
        processor.sendValue(name + resources.getString(R.string.parameter_sustain), sustain);
        processor.sendValue(name + resources.getString(R.string.parameter_release), release);
        processor.sendValue(name + resources.getString(R.string.parameter_gate), gate);
    }

    @Override
    public String getTypeName() {
        return "eg";
    }

    @Override
    public int getNumberOfInputs() {
        return 1;
    }

    @Override
    public int getNumberOfOutputs() {
        return 1;
    }
}
