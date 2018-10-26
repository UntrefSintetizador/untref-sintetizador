package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.entities.NGPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchNGPresenter extends PatchPresenter {

    public PatchNGPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }


    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((NGPatch) patch).on_off, new LinearFunction(0f, 1f));
        return true;
    }
}
