package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchSHPresenter;

public class PatchSHView extends PatchView {


    public PatchSHView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_sh;
        topImage = R.drawable.map_node_in_sh;
        bottomImage = R.drawable.map_node_out_sh;
        color = Color.rgb(253, 180, 0);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter, Patch patch) {
        return new PatchSHPresenter(this, patchGraphPresenter, patch);
    }
}
