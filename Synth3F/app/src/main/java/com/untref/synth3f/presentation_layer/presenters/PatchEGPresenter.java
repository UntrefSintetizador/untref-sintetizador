package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.EGPatch;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchEGPresenter extends PatchPresenter {

    public PatchEGPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter,
                            Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        EGPatch egPatch = (EGPatch) patch;
        Parameter attack = new Parameter(patchMenuView.getResources().getString(R.string.parameter_attack), egPatch.attack, FLOAT_PRECISION,
                new ExponentialLeftFunction(0f, 5000f));
        Parameter decay = new Parameter(patchMenuView.getResources().getString(R.string.parameter_decay), egPatch.decay, FLOAT_PRECISION,
                new ExponentialLeftFunction(0f, 5000f));
        Parameter sustain = new Parameter(patchMenuView.getResources().getString(R.string.parameter_sustain), egPatch.sustain, FLOAT_PRECISION,
                new ExponentialLeftFunction(0f, 5000f));
        Parameter release = new Parameter(patchMenuView.getResources().getString(R.string.parameter_release), egPatch.release, FLOAT_PRECISION,
                new ExponentialLeftFunction(0f, 5000f));
        patchMenuView.createEnvelopeEditor(attack, decay, sustain, release);
        return true;
    }
}
