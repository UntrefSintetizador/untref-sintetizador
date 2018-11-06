package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchVCFPresenter extends PatchPresenter {

    public PatchVCFPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        patchMenuView.createKnob("on-off", INTEGER_PRECISION, ((VCFPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView.createKnob("att_signal", FLOAT_PRECISION, ((VCFPatch) patch).att_signal, new ExponentialLeftFunction(0f, 1000f));
        patchMenuView.createKnob("att_freq", FLOAT_PRECISION, ((VCFPatch) patch).att_freq, new ExponentialLeftFunction(0f, 1000f));
        int[] imageIds = {R.drawable.edit_vcf_bandpass, R.drawable.edit_vcf_lowpass, R.drawable.edit_vcf_highpass};
        patchMenuView.createOptionList("mode", imageIds, (int) ((VCFPatch) patch).mode);
        patchMenuView.createKnob("freq", FLOAT_PRECISION, ((VCFPatch) patch).freq, new ExponentialLeftFunction(0f, 15000f));
        patchMenuView.createKnob("q", FLOAT_PRECISION, ((VCFPatch) patch).q, new LinearFunction(0f, 100f));
        return true;
    }
}
