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
        patchMenuView2.createKnob("on-off", 1f, 0f, INTEGER_PRECISION, ((SHPatch) patch).on_off, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("att_signal", 100f, -100f, FLOAT_PRECISION, ((SHPatch) patch).att_signal, PatchMenuView.MenuScale.exponential_center);
        patchMenuView2.createKnob("glide", 5000f, 0f, FLOAT_PRECISION, ((SHPatch) patch).glide, PatchMenuView.MenuScale.exponential_left);
    }
}
