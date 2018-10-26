package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchKBPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchKBView extends PatchView {


    public PatchKBView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_kb;
        topImage = R.drawable.map_node_out_kb;
        bottomImage = R.drawable.map_node_out_kb;
        color = Color.rgb(178, 0, 255);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter, Patch patch) {
        return new PatchKBPresenter(this, patchGraphPresenter, patch);
    }
}
