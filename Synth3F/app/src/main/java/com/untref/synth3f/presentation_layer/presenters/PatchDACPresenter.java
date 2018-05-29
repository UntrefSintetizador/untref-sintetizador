package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.View.PatchDACMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchVCOMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchDACPresenter extends PatchPresenter {

    public PatchDACPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter) {
        super(patchView, patchGraphPresenter);
        numberOfInputs = 2;
        numberOfOutputs = 0;
        name = "dac_";
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchDACMenuView(context, R.layout.popup_vco , name + Integer.toString(patchView.getPatchId()), this);
    }
}
