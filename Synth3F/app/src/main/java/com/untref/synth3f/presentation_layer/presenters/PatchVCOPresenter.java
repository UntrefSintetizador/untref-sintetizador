package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
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
        float patchFreq = ((VCOPatch) patch).freq;

        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((VCOPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView.createKnob("att_freq0", FLOAT_PRECISION, ((VCOPatch) patch).att_freq0, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob("att_freq1", FLOAT_PRECISION, ((VCOPatch) patch).att_freq1, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob("att_pw", FLOAT_PRECISION, ((VCOPatch) patch).att_pw, new ExponentialCenterFunction(-100f, 100f));
        int[] imageIds = {R.drawable.edit_vco_sine, R.drawable.edit_vco_isaw, R.drawable.edit_vco_saw, R.drawable.edit_vco_triangle, R.drawable.edit_vco_square};
        patchMenuView.createOptionList("shape", imageIds, (int) ((VCOPatch) patch).shape);
        patchMenuView.createKnob("freq", FLOAT_PRECISION, patchFreq, new ExponentialLeftFunction(0f, 20000f));
        patchMenuView.createKnob("offset", INTEGER_PRECISION, ((VCOPatch) patch).offset, new LinearFunction(-64f, 63f));
        patchMenuView.linkKnobs("freq", "offset", new FrequencyOffsetFunction(patchFreq), new OffsetFrequencyFunction(patchFreq));
        patchMenuView.createKnob("pw", FLOAT_PRECISION, ((VCOPatch) patch).pw, new LinearFunction(0f, 100f));
        return true;
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
