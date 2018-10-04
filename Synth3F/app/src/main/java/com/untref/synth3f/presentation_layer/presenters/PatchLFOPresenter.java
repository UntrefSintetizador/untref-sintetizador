package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.LFOPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchLFOMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchLFOPresenter extends PatchPresenter {

    public PatchLFOPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchLFOMenuView(context, R.layout.popup_lfo, this, patch);
    }

    @Override
    public void initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", 1f, 0f, INTEGER_PRECISION, ((LFOPatch) patch).on_off, PatchMenuView.MenuScale.linear);
        patchMenuView2.createKnob("att_freq0", 100f, -100f, FLOAT_PRECISION, ((LFOPatch) patch).att_freq0, PatchMenuView.MenuScale.exponential_center);
        patchMenuView2.createKnob("att_pw", 100f, -100f, FLOAT_PRECISION, ((LFOPatch) patch).att_pw, PatchMenuView.MenuScale.exponential_center);
        int[] imageIds = {R.drawable.edit_lfo_sine, R.drawable.edit_lfo_isaw, R.drawable.edit_lfo_saw, R.drawable.edit_lfo_triangle, R.drawable.edit_lfo_square};
        patchMenuView2.createOptionList("shape", imageIds, (int) ((LFOPatch) patch).shape);
        patchMenuView2.createKnob("freq", 100f, 0f, FLOAT_PRECISION, ((LFOPatch) patch).freq, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("BPM", 6000f, 0f, FLOAT_PRECISION, ((LFOPatch) patch).BPM, PatchMenuView.MenuScale.exponential_left);
        patchMenuView2.createKnob("pw", 100f, 0f, FLOAT_PRECISION, ((LFOPatch) patch).pw, PatchMenuView.MenuScale.linear);
    }
}
