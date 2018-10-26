package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.entities.MIXPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchMIXPresenter extends PatchPresenter {

    public PatchMIXPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        MenuScaleFunction linearFunction = new LinearFunction(0f, 1f);
        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((MIXPatch) patch).on_off, linearFunction);
        patchMenuView.createKnob("ch1", FLOAT_PRECISION, ((MIXPatch) patch).ch1, linearFunction);
        patchMenuView.createKnob("ch2", FLOAT_PRECISION, ((MIXPatch) patch).ch2, linearFunction);
        patchMenuView.createKnob("ch3", FLOAT_PRECISION, ((MIXPatch) patch).ch3, linearFunction);
        patchMenuView.createKnob("ch4", FLOAT_PRECISION, ((MIXPatch) patch).ch4, linearFunction);
        patchMenuView.createKnob("master", FLOAT_PRECISION, ((MIXPatch) patch).master, linearFunction);
        return true;
    }
}
