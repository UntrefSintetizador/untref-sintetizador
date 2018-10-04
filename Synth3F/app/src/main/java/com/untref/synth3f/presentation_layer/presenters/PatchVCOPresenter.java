package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCOPatch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchVCOMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchVCOPresenter extends PatchPresenter {

    public PatchVCOPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchVCOMenuView(context, R.layout.popup_vco, this, patch);
    }

    @Override
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", 1f, 0f, INTEGER_PRECISION, ((VCOPatch) patch).on_off, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("att_freq0", 100.0f, -100.0f, FLOAT_PRECISION, ((VCOPatch) patch).att_freq0, PatchMenuView.MenuScale.exponential_center);
        patchMenuView2.createKnob("att_freq1", 100.0f, -100.0f, FLOAT_PRECISION, ((VCOPatch) patch).att_freq1, PatchMenuView.MenuScale.exponential_center);
        patchMenuView2.createKnob("att_pw", 100.0f, -100.0f, FLOAT_PRECISION, ((VCOPatch) patch).att_pw, PatchMenuView.MenuScale.exponential_center);
        int[] imageIds = {R.drawable.edit_vco_sine, R.drawable.edit_vco_isaw, R.drawable.edit_vco_saw, R.drawable.edit_vco_triangle, R.drawable.edit_vco_square};
        patchMenuView2.createOptionList("shape", imageIds, (int) ((VCOPatch) patch).shape);
        patchMenuView2.createKnob("freq", 20000.0f, 0.0f, FLOAT_PRECISION, ((VCOPatch) patch).freq, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("offset", 63.0f, -64.0f, FLOAT_PRECISION, ((VCOPatch) patch).offset, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("pw", 100.0f, 0.0f, FLOAT_PRECISION, ((VCOPatch) patch).pw, PatchMenuView.MenuScale.linear);
    }
}
