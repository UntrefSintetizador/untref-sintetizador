package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchVCOMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchVCOPresenter extends PatchPresenter {

    public PatchVCOPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
        numberOfInputs = 3;
        numberOfOutputs = 1;
        name = "vco_";
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchVCOMenuView(context, R.layout.popup_vco, name + Integer.toString(patchView.getPatchId()), this, patch);
    }
}
