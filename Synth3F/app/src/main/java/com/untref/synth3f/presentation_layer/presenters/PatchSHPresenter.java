package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.SHPatch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchSHPresenter extends PatchPresenter {

    public PatchSHPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        SHPatch shPatch = (SHPatch) patch; 
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off), shPatch.on_off, INTEGER_PRECISION,
                                        new LinearFunction(0f, 1f));
        patchMenuView.createKnob(onOff);
        Parameter attSignal = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_signal), shPatch.att_signal, FLOAT_PRECISION,
                                            new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attSignal);
        Parameter glide = new Parameter(patchMenuView.getResources().getString(R.string.parameter_glide), shPatch.glide, FLOAT_PRECISION,
                                        new ExponentialLeftFunction(0f, 5000f));
        patchMenuView.createKnob(glide);
        return true;
    }
}
