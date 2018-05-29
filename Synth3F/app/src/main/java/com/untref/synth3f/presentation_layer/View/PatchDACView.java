package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.presenters.PatchDACPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchVCOPresenter;

public class PatchDACView extends PatchView {


    public PatchDACView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter) {
        super(context, wireDrawer, patchGraphPresenter);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.drag_dac;
        topImage = R.drawable.map_node_in_vco;
        bottomImage = R.drawable.map_node_out_vco;
        color = Color.MAGENTA;
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter) {
        return new PatchDACPresenter(this, patchGraphPresenter);
    }
}
