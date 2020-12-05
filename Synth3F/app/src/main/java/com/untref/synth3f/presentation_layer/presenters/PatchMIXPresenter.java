package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.MIXPatch;
import com.untref.synth3f.entities.Parameter;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

public class PatchMIXPresenter extends PatchPresenter {

    public PatchMIXPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter,
                             Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        MIXPatch mixPatch = (MIXPatch) patch;
        MenuScaleFunction linearFunction = new LinearFunction(0f, 1f);
        Parameter onOff = new Parameter(patchMenuView.getResources().getString(R.string.parameter_on_off), mixPatch.on_off, INTEGER_PRECISION,
                                        linearFunction);
        patchMenuView.createKnob(onOff);
        Parameter ch1 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_ch1), mixPatch.ch1, FLOAT_PRECISION, linearFunction);
        patchMenuView.createKnob(ch1);
        Parameter ch2 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_ch2), mixPatch.ch2, FLOAT_PRECISION, linearFunction);
        patchMenuView.createKnob(ch2);
        Parameter ch3 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_ch3), mixPatch.ch3, FLOAT_PRECISION, linearFunction);
        patchMenuView.createKnob(ch3);
        Parameter ch4 = new Parameter(patchMenuView.getResources().getString(R.string.parameter_ch4), mixPatch.ch4, FLOAT_PRECISION, linearFunction);
        patchMenuView.createKnob(ch4);
        Parameter master = new Parameter(patchMenuView.getResources().getString(R.string.parameter_master), mixPatch.master, FLOAT_PRECISION,
                                         linearFunction);
        patchMenuView.createKnob(master);
        return true;
    }
}
