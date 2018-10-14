package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchVCFMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchVCFPresenter extends PatchPresenter {

    public PatchVCFPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchVCFMenuView(context, R.layout.popup_vcf, this, patch);
    }

    @Override
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((VCFPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView2.createKnob("att_signal", FLOAT_PRECISION, ((VCFPatch) patch).att_signal, new ExponentialLeftFunction(0f, 1000f));
        patchMenuView2.createKnob("att_freq", FLOAT_PRECISION, ((VCFPatch) patch).att_freq, new ExponentialLeftFunction(0f, 1000f));
        int[] imageIds = {R.drawable.edit_vcf_bandpass, R.drawable.edit_vcf_lowpass, R.drawable.edit_vcf_highpass};
        patchMenuView2.createOptionList("mode", imageIds, (int) ((VCFPatch) patch).mode);
        patchMenuView2.createKnob("mode", INTEGER_PRECISION, ((VCFPatch) patch).mode, new LinearFunction(0f, 2f));
        patchMenuView2.createKnob("freq", FLOAT_PRECISION, ((VCFPatch) patch).freq, new ExponentialLeftFunction(0f, 15000f));
        patchMenuView2.createKnob("q", FLOAT_PRECISION, ((VCFPatch) patch).q, new LinearFunction(0f, 100f));
    }
}
