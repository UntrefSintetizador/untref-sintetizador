package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.EGPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchEGMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchEGPresenter extends PatchPresenter {

    public PatchEGPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchEGMenuView(context, R.layout.popup_eg, this, patch);
    }

    @Override
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", 1.0f, 0.0f, INTEGER_PRECISION, ((EGPatch) patch).on_off, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("attack", 5000.0f, 0.0f, FLOAT_PRECISION, ((EGPatch) patch).attack, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("decay", 5000.0f, 0.0f, FLOAT_PRECISION, ((EGPatch) patch).decay, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("sustain", 1.0f, 0.0f, FLOAT_PRECISION, ((EGPatch) patch).sustain, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("release", 5000.0f, 0.0f, FLOAT_PRECISION, ((EGPatch) patch).release, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("gate", 1.0f, 0.0f, INTEGER_PRECISION, ((EGPatch) patch).gate, PatchMenuView.MenuScale.linear);
    }
}
