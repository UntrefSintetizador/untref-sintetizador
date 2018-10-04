package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.MIXPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMIXMenuView;
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
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", 1f, 0f, INTEGER_PRECISION, ((MIXPatch) patch).on_off, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("ch1", 1f, 0f, FLOAT_PRECISION, ((MIXPatch) patch).ch1, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("ch2", 1f, 0f, FLOAT_PRECISION, ((MIXPatch) patch).ch2, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("ch3", 1f, 0f, FLOAT_PRECISION, ((MIXPatch) patch).ch3, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("ch4", 1f, 0f, FLOAT_PRECISION, ((MIXPatch) patch).ch4, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("master", 1f, 0f, FLOAT_PRECISION, ((MIXPatch) patch).master, PatchMenuView.MenuScale.linear);
    }
}
