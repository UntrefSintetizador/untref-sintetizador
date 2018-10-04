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
        patchMenuView2.createKnob("on-off", 1.0f, 0.0f, INTEGER_PRECISION, ((VCFPatch) patch).on_off, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("att_signal", 1000.0f, 0.0f, FLOAT_PRECISION, ((VCFPatch) patch).att_signal, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("att_freq", 1000.0f, 0.0f, FLOAT_PRECISION, ((VCFPatch) patch).att_freq, PatchMenuView.MenuScale.exponential_left);
        int[] imageIds = {R.drawable.edit_vcf_bandpass, R.drawable.edit_vcf_lowpass, R.drawable.edit_vcf_highpass};
        patchMenuView2.createOptionList("mode", imageIds, (int) ((VCFPatch) patch).mode);
        patchMenuView2.createKnob("mode", 2f, 0f, INTEGER_PRECISION, ((VCFPatch) patch).mode, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("freq", 15000.0f, 0.0f, FLOAT_PRECISION, ((VCFPatch) patch).freq, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("q", 100.0f, 0.0f, FLOAT_PRECISION, ((VCFPatch) patch).q, PatchMenuView.MenuScale.linear);
    }
}
