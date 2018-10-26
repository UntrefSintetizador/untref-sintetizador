package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCAPatch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchVCAPresenter extends PatchPresenter {

    public PatchVCAPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        MenuScaleFunction linearFunction = new LinearFunction(0f, 1f);
        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((VCAPatch) patch).on_off, linearFunction);
        patchMenuView.createKnob("att_control", FLOAT_PRECISION, ((VCAPatch) patch).att_control, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob("base", FLOAT_PRECISION, ((VCAPatch) patch).base, linearFunction);
        patchMenuView.createKnob("clip", INTEGER_PRECISION, ((VCAPatch) patch).clip, linearFunction);
        return true;
    }
}
