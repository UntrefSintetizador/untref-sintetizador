package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.LFOPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.LinkingFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchLFOPresenter extends PatchPresenter {

    public PatchLFOPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((LFOPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView.createKnob("att_freq0", FLOAT_PRECISION, ((LFOPatch) patch).att_freq0, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob("att_pw", FLOAT_PRECISION, ((LFOPatch) patch).att_pw, new ExponentialCenterFunction(-100f, 100f));
        int[] imageIds = {R.drawable.edit_lfo_sine, R.drawable.edit_lfo_isaw, R.drawable.edit_lfo_saw, R.drawable.edit_lfo_triangle, R.drawable.edit_lfo_square};
        patchMenuView.createOptionList("shape", imageIds, (int) ((LFOPatch) patch).shape);
        patchMenuView.createKnob("freq", FLOAT_PRECISION, ((LFOPatch) patch).freq, new ExponentialLeftFunction(0f, 100f));
        patchMenuView.createKnob("BPM", FLOAT_PRECISION, ((LFOPatch) patch).BPM, new ExponentialLeftFunction(0f, 6000f));
        patchMenuView.linkKnobs("BPM", "freq", new FreqToBPM(), new BPMToFreq());
        patchMenuView.createKnob("pw", FLOAT_PRECISION, ((LFOPatch) patch).pw, new LinearFunction(0f, 100f));
        return true;
    }

    private static class BPMToFreq implements LinkingFunction {

        @Override
        public float calculate(Float x) {
            return x / 60;
        }
    }

    private static class FreqToBPM implements LinkingFunction {

        @Override
        public float calculate(Float x) {
            return x * 60;
        }
    }
}
