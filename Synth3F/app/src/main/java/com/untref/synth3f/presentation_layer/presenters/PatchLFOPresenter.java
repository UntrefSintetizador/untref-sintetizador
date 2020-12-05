package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.LFOPatch;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.LinkingFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchLFOPresenter extends PatchPresenter {

    public PatchLFOPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter,
                             Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        LFOPatch lfoPatch = (LFOPatch) patch;
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off), lfoPatch.on_off, INTEGER_PRECISION,
                new LinearFunction(0f, 1f));
        patchMenuView.createKnob(onOff);
        Parameter attFreq0 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_freq0), lfoPatch.att_freq0, FLOAT_PRECISION,
                new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attFreq0);
        Parameter attPw = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_pw), lfoPatch.att_pw, FLOAT_PRECISION,
                new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attPw);
        Parameter freq = new Parameter(patchMenuView.getResources().getString(R.string.parameter_freq), lfoPatch.freq, FLOAT_PRECISION,
                new ExponentialLeftFunction(0f, 100f));
        patchMenuView.createKnob(freq);
        Parameter bpm = new Parameter(patchMenuView.getResources().getString(R.string.parameter_bpm), lfoPatch.BPM, FLOAT_PRECISION,
                new ExponentialLeftFunction(0f, 6000f));
        patchMenuView.createKnob(bpm);
        patchMenuView.linkKnobs(bpm.getName(), freq.getName(), new FreqToBPM(), new BPMToFreq());
        Parameter pw = new Parameter(patchMenuView.getResources().getString(R.string.parameter_pw), lfoPatch.pw, FLOAT_PRECISION,
                new LinearFunction(0f, 100f));
        patchMenuView.createKnob(pw);
        createOptionList(patchMenuView, lfoPatch);
        return true;
    }

    private void createOptionList(PatchMenuView patchMenuView, LFOPatch lfoPatch) {
        int[] iconOffIds = {R.drawable.ic_lfo_sine_off, R.drawable.ic_lfo_isaw_off,
                            R.drawable.ic_lfo_saw_off, R.drawable.ic_lfo_triangle_off,
                            R.drawable.ic_lfo_square_off};
        int[] iconOnIds = {R.drawable.ic_osc_sine_on, R.drawable.ic_osc_isaw_on,
                           R.drawable.ic_osc_saw_on, R.drawable.ic_osc_triangle_on,
                           R.drawable.ic_osc_square_on};
        patchMenuView.createOptionList(patchMenuView.getResources().getString(R.string.parameter_shape), iconOffIds, iconOnIds, (int) lfoPatch.shape);
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
