package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCOPatch;
import com.untref.synth3f.presentation_layer.View.LinkingFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchVCOPresenter extends PatchPresenter {

    public PatchVCOPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }


    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        VCOPatch vcoPatch = (VCOPatch) patch;
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off), vcoPatch.on_off, INTEGER_PRECISION,
                                        new LinearFunction(0f, 1f));
        patchMenuView.createKnob(onOff);
        Parameter attFreq0 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_freq0), vcoPatch.att_freq0, FLOAT_PRECISION,
                                            new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attFreq0);
        Parameter attFreq1 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_freq1), vcoPatch.att_freq1, FLOAT_PRECISION,
                                           new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attFreq1);
        Parameter attPw = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_pw), vcoPatch.att_pw, FLOAT_PRECISION,
                                        new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attPw);
        createOptionList(patchMenuView, vcoPatch);
        createLinkedKnobs(patchMenuView, vcoPatch);
        Parameter pw = new Parameter(patchMenuView.getResources().getString(R.string.parameter_pw), vcoPatch.pw, FLOAT_PRECISION,
                                     new LinearFunction(0f, 100f));
        patchMenuView.createKnob(pw);
        return true;
    }

    private void createOptionList(PatchMenuView patchMenuView, VCOPatch vcoPatch) {
        int[] iconOffIds = {R.drawable.ic_osc_sine_off, R.drawable.ic_osc_isaw_off,
                            R.drawable.ic_osc_saw_off, R.drawable.ic_osc_triangle_off,
                            R.drawable.ic_osc_square_off};
        int[] iconOnIds = {R.drawable.ic_osc_sine_on, R.drawable.ic_osc_isaw_on,
                           R.drawable.ic_osc_saw_on, R.drawable.ic_osc_triangle_on,
                           R.drawable.ic_osc_square_on};
        patchMenuView.createOptionList(patchMenuView.getResources().getString(R.string.parameter_shape), iconOffIds, iconOnIds, (int) vcoPatch.shape);
    }

    private void createLinkedKnobs(PatchMenuView patchMenuView, VCOPatch vcoPatch) {
        Parameter freq = new Parameter(patchMenuView.getResources().getString(R.string.parameter_freq), vcoPatch.freq, FLOAT_PRECISION,
                                       new ExponentialLeftFunction(0f, 20000f));
        patchMenuView.createKnob(freq);
        Parameter offset = new Parameter(patchMenuView.getResources().getString(R.string.parameter_offset), vcoPatch.offset, INTEGER_PRECISION,
                                         new LinearFunction(-64f, 63f));
        patchMenuView.createKnob(offset);
        patchMenuView.linkKnobs(freq.getName(), offset.getName(),
                                new FrequencyOffsetFunction(freq.getValue()),
                                new OffsetFrequencyFunction(freq.getValue()));
    }

    private static class FrequencyOffsetFunction implements LinkingFunction {

        private final float initialFreq;

        public FrequencyOffsetFunction(float initialFreq) {
            this.initialFreq = initialFreq;
        }

        @Override
        public float calculate(Float x) {
            return (float) Math.pow(2, x / 12) * initialFreq;
        }
    }

    private static class OffsetFrequencyFunction implements LinkingFunction {

        private final float initialFreq;

        public OffsetFrequencyFunction(float initialFreq) {
            this.initialFreq = initialFreq;
        }

        @Override
        public float calculate(Float x) {
            return 12f * (float) (Math.log(x / initialFreq) / Math.log(2));
        }
    }
}
