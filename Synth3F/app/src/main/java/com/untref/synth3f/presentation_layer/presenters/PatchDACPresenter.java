package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchDACPresenter extends PatchPresenter {

    public PatchDACPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter,
                             Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off), ((DACPatch) patch).on_off,
                                        INTEGER_PRECISION, new LinearFunction(0f, 1f));
        patchMenuView.createKnob(onOff);
        return true;
    }
}
