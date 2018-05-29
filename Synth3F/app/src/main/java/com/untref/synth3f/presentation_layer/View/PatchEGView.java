package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.presenters.PatchEGPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchVCOPresenter;

public class PatchEGView extends PatchView {


    public PatchEGView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter) {
        super(context, wireDrawer, patchGraphPresenter);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_eg;
        topImage = R.drawable.map_node_in_eg;
        bottomImage = R.drawable.map_node_out_eg;
        color = Color.rgb(255,107,53);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter) {
        return new PatchEGPresenter(this, patchGraphPresenter);
    }
}
