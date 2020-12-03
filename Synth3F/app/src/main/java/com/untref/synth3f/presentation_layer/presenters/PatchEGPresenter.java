package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.entities.EGPatch;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchEGPresenter extends PatchPresenter {

    public PatchEGPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
//        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((EGPatch) patch).on_off, new LinearFunction(0f, 1f));
//        patchMenuView.createKnob("attack", FLOAT_PRECISION, ((EGPatch) patch).attack, new ExponentialLeftFunction(0f, 5000f));
//        patchMenuView.createKnob("decay", FLOAT_PRECISION, ((EGPatch) patch).decay, new ExponentialLeftFunction(0f, 5000f));
//        patchMenuView.createKnob("sustain", FLOAT_PRECISION, ((EGPatch) patch).sustain, new LinearFunction(0f, 1f));
//        patchMenuView.createKnob("release", FLOAT_PRECISION, ((EGPatch) patch).release, new ExponentialLeftFunction(0f, 5000f));
//        patchMenuView.createKnob("gate", INTEGER_PRECISION, ((EGPatch) patch).gate, new LinearFunction(0f, 1f));
        EGPatch egPatch = (EGPatch) patch;
        Parameter attack = new Parameter("attack", egPatch.attack, FLOAT_PRECISION,
                                         new ExponentialLeftFunction(0f, 5000f));
        Parameter decay = new Parameter("decay", egPatch.decay, FLOAT_PRECISION,
                                        new ExponentialLeftFunction(0f, 5000f));
        Parameter sustain = new Parameter("sustain", egPatch.sustain, FLOAT_PRECISION,
                                          new ExponentialLeftFunction(0f, 5000f));
        Parameter release = new Parameter("release", egPatch.release, FLOAT_PRECISION,
                                          new ExponentialLeftFunction(0f, 5000f));
        patchMenuView.createEnvelopeEditor(attack, decay, sustain, release);
        return true;
    }
}
