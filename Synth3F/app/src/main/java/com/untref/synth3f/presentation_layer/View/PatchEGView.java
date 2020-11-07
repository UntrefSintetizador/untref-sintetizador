package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchEGPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchEGView extends PatchView {


    public PatchEGView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_eg;
        topImage = R.drawable.map_node_in_eg;
        bottomImage = R.drawable.map_node_out_eg;
        color = Color.rgb(255, 107, 53);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter, Patch patch) {
        return new PatchEGPresenter(this, patchGraphPresenter, patch);
    }
}
