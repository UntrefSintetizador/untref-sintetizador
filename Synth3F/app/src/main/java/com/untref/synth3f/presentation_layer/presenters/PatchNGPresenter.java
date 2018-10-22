package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.NGPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchNGMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchNGPresenter extends PatchPresenter {

    public PatchNGPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchNGMenuView(context, R.layout.popup_ng, this, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView2 patchMenuView2) {
        patchMenuView2.createKnob("on-off", INTEGER_PRECISION, ((NGPatch) patch).on_off, new LinearFunction(0f, 1f));
        return true;
    }
}
