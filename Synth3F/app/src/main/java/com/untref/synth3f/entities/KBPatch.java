package com.untref.synth3f.entities;

import android.content.res.Resources;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Processor;

public class KBPatch extends Patch {

    public float on_off = 1f;
    public float gate = 1f;
    public float midi_note = 0f;
    public float glide = 0f;
    public int octave = 4;

    @Override
    public void initialize(Processor processor, Resources resources) {
        String name = resources.getString(R.string.pd_patch_name_prefix) + getTypeName() + "_" + getId() + "_";
        processor.sendValue(name + resources.getString(R.string.parameter_on_off), on_off);
        processor.sendValue(name + resources.getString(R.string.parameter_gate), gate);
        processor.sendValue(name + resources.getString(R.string.parameter_midi_note), midi_note);
        processor.sendValue(name + resources.getString(R.string.parameter_glide), glide);
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
