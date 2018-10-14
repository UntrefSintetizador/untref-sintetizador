package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCAPatch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchVCAMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchVCAPresenter extends PatchPresenter {

    public PatchVCAPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchVCAMenuView(context, R.layout.popup_vca, this, patch);
    }

    @Override
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        MenuScaleFunction linearFunction = new LinearFunction(0f, 1f);
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((VCAPatch) patch).on_off, linearFunction);
        patchMenuView2.createKnob("att_control", FLOAT_PRECISION, ((VCAPatch) patch).att_control, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView2.createKnob("base", FLOAT_PRECISION, ((VCAPatch) patch).base, linearFunction);
        patchMenuView2.createKnob("clip", INTEGER_PRECISION, ((VCAPatch) patch).clip, linearFunction);
    }
}
