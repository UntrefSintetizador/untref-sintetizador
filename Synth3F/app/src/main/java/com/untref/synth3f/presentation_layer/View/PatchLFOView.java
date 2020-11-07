package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchLFOPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchLFOView extends PatchView {


    public PatchLFOView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_lfo;
        topImage = R.drawable.map_node_in_lfo;
        bottomImage = R.drawable.map_node_out_lfo;
        color = Color.rgb(253, 180, 0);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter, Patch patch) {
        return new PatchLFOPresenter(this, patchGraphPresenter, patch);
    }
}
