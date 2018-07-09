package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchVCFMenuView;
import com.untref.synth3f.presentation_layer.View.PatchVCOMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchVCFPresenter extends PatchPresenter {

    public PatchVCFPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
        numberOfInputs = 2;
        numberOfOutputs = 1;
        name = "vcf_";
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchVCFMenuView(context, R.layout.popup_vcf, name + Integer.toString(patchView.getPatchId()), this, patch);
    }
}
