package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchKBMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView2;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public class PatchKBPresenter extends PatchPresenter {

    public PatchKBPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(patchView, patchGraphPresenter, patch);
    }

    @Override
    public PatchMenuView createMenuView(MainActivity context) {
        return new PatchKBMenuView(context, R.layout.popup_kb, this, patch);
    }

    @Override
    public boolean initMenuView(PatchMenuView2 patchMenuView2) {
        PatchMenuView patchMenuView = createMenuView((MainActivity) patchMenuView2.getPatchGraphFragment().getActivity());
        patchMenuView.showAsDropDown(patchView, 150, -500);
        patchMenuView.setButton(patchView);
        return false;
    }
}
