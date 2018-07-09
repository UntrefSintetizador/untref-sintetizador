package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchEGMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchVCOMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchEGPresenter extends PatchPresenter {

    public PatchEGPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
        numberOfInputs = 1;
        numberOfOutputs = 2;
        name = "eg_";
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchEGMenuView(context, R.layout.popup_eg, name + Integer.toString(patchView.getPatchId()), this, patch);
    }
}
