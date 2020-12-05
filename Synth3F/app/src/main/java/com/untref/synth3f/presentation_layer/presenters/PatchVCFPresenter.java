package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchVCFPresenter extends PatchPresenter {

    public PatchVCFPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter,
                             Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        VCFPatch vcfPatch = (VCFPatch) patch;
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off), vcfPatch.on_off, INTEGER_PRECISION,
                                        new LinearFunction(0f, 1f));
        patchMenuView.createKnob(onOff);
        Parameter attSignal = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_signal), vcfPatch.att_signal, FLOAT_PRECISION,
                                            new ExponentialLeftFunction(0f, 1000f));
        patchMenuView.createKnob(attSignal);
        Parameter attFreq = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_freq), vcfPatch.att_freq, FLOAT_PRECISION,
                                          new ExponentialLeftFunction(0f, 1000f));
        patchMenuView.createKnob(attFreq);
        createOptionList(patchMenuView, vcfPatch);
        Parameter freq = new Parameter(patchMenuView.getResources().getString(R.string.parameter_freq), vcfPatch.freq, FLOAT_PRECISION,
                                       new ExponentialLeftFunction(0f, 15000f));
        patchMenuView.createKnob(freq);
        Parameter q = new Parameter(patchMenuView.getResources().getString(R.string.parameter_q), vcfPatch.q, FLOAT_PRECISION,
                                    new LinearFunction(0f, 100f));
        patchMenuView.createKnob(q);
        return true;
    }

    private void createOptionList(PatchMenuView patchMenuView, VCFPatch vcfPatch) {
        int[] iconOffIds = {R.drawable.ic_filter_bp_off, R.drawable.ic_filter_lp_off,
                            R.drawable.ic_filter_hp_off};
        int[] iconOnIds = {R.drawable.ic_filter_bp_on, R.drawable.ic_filter_lp_on,
                           R.drawable.ic_filter_hp_on};
        patchMenuView.createOptionList(patchMenuView.getResources().getString(R.string.parameter_mode), iconOffIds, iconOnIds, (int) vcfPatch.mode);
    }
}
