package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCAPatch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchVCAPresenter extends PatchPresenter {

    public PatchVCAPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        VCAPatch vcaPatch = (VCAPatch) patch; 
        MenuScaleFunction linearFunction = new LinearFunction(0f, 1f);
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off),
                                        vcaPatch.on_off, INTEGER_PRECISION, linearFunction);
        patchMenuView.createKnob(onOff);
        Parameter attControl = new Parameter(patchMenuView.getResources().getString(R.string.parameter_att_control), vcaPatch.att_control, FLOAT_PRECISION,
                                             new ExponentialCenterFunction(-100f, 100f));
        patchMenuView.createKnob(attControl);
        Parameter base = new Parameter(patchMenuView.getResources().getString(R.string.parameter_base), vcaPatch.base, FLOAT_PRECISION, linearFunction);
        patchMenuView.createKnob(base);
        Parameter clip = new Parameter(patchMenuView.getResources().getString(R.string.parameter_clip), vcaPatch.clip, INTEGER_PRECISION, linearFunction);
        patchMenuView.createKnob(clip);
        return true;
    }
}
