package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.SHPatch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchSHMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchSHPresenter extends PatchPresenter {

    public PatchSHPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchSHMenuView(context, R.layout.popup_sh, this, patch);
    }

    @Override
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((SHPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView2.createKnob("att_signal", FLOAT_PRECISION, ((SHPatch) patch).att_signal, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView2.createKnob("glide", FLOAT_PRECISION, ((SHPatch) patch).glide, new ExponentialLeftFunction(0f, 5000f));
    }
}
