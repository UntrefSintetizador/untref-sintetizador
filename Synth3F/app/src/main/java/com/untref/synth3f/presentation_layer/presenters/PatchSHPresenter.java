package com.untref.synth3f.presentation_layer.presenters;

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
        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((SHPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView.createKnob("att_signal", FLOAT_PRECISION, ((SHPatch) patch).att_signal, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob("glide", FLOAT_PRECISION, ((SHPatch) patch).glide, new ExponentialLeftFunction(0f, 5000f));
        return true;
    }
}
