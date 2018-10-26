package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchKBMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchKBPresenter extends PatchPresenter {

    public PatchKBPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView patchMenuView) {
        PatchKBMenuView patchKBMenuView = new PatchKBMenuView((MainActivity) patchMenuView.getPatchGraphFragment().getActivity(), R.layout.popup_kb, this, patch);
        patchKBMenuView.showAsDropDown(patchView, 150, -500);
        patchKBMenuView.setButton(patchView);
        return false;
    }
}
