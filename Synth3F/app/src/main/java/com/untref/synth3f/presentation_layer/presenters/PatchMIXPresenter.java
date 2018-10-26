package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.MIXPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMIXMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchMIXPresenter extends PatchPresenter {

    public PatchMIXPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchMIXMenuView(context, R.layout.popup_mix, this, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView2 patchMenuView2) {
        MenuScaleFunction linearFunction = new LinearFunction(0f, 1f);
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((MIXPatch) patch).on_off, linearFunction);
        patchMenuView2.createKnob("ch1", FLOAT_PRECISION, ((MIXPatch) patch).ch1, linearFunction);
        patchMenuView2.createKnob("ch2", FLOAT_PRECISION, ((MIXPatch) patch).ch2, linearFunction);
        patchMenuView2.createKnob("ch3", FLOAT_PRECISION, ((MIXPatch) patch).ch3, linearFunction);
        patchMenuView2.createKnob("ch4", FLOAT_PRECISION, ((MIXPatch) patch).ch4, linearFunction);
        patchMenuView2.createKnob("master", FLOAT_PRECISION, ((MIXPatch) patch).master, linearFunction);
        return true;
    }
}
