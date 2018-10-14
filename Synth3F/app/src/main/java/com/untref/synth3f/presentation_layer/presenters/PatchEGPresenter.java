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
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((EGPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView2.createKnob("attack", FLOAT_PRECISION, ((EGPatch) patch).attack, new ExponentialLeftFunction(0f, 5000f));
        patchMenuView2.createKnob("decay", FLOAT_PRECISION, ((EGPatch) patch).decay, new ExponentialLeftFunction(0f, 5000f));
        patchMenuView2.createKnob("sustain", FLOAT_PRECISION, ((EGPatch) patch).sustain, new LinearFunction(0f, 1f));
        patchMenuView2.createKnob("release", FLOAT_PRECISION, ((EGPatch) patch).release, new ExponentialLeftFunction(0f, 5000f));
        patchMenuView2.createKnob("gate", INTEGER_PRECISION, ((EGPatch) patch).gate, new LinearFunction(0f, 1f));
    }
}
