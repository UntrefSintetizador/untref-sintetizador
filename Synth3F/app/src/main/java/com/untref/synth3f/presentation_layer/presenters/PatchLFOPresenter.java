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
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((LFOPatch) patch).on_off, new LinearFunction(0f, 1f));
        patchMenuView2.createKnob("att_freq0", FLOAT_PRECISION, ((LFOPatch) patch).att_freq0, new ExponentialCenterFunction(-100f, 100f));
        patchMenuView2.createKnob("att_pw", FLOAT_PRECISION, ((LFOPatch) patch).att_pw, new ExponentialCenterFunction(-100f, 100f));
        int[] imageIds = {R.drawable.edit_lfo_sine, R.drawable.edit_lfo_isaw, R.drawable.edit_lfo_saw, R.drawable.edit_lfo_triangle, R.drawable.edit_lfo_square};
        patchMenuView2.createOptionList("shape", imageIds, (int) ((LFOPatch) patch).shape);
        patchMenuView2.createKnob("freq", FLOAT_PRECISION, ((LFOPatch) patch).freq, new ExponentialLeftFunction(0f, 100f));
        patchMenuView2.createKnob("BPM", FLOAT_PRECISION, ((LFOPatch) patch).BPM, new ExponentialLeftFunction(0f, 6000f));
        patchMenuView2.createKnob("pw", FLOAT_PRECISION, ((LFOPatch) patch).pw, new LinearFunction(0f, 100f));
    }
}
