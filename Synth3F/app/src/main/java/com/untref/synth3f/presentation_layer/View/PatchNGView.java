package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchNGPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchNGView extends PatchView {

    public PatchNGView(Context context, WireDrawer wireDrawer,
                       PatchGraphPresenter patchGraphPresenter,
                       PatchGraphFragment patchGraphFragment, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patchGraphFragment, patch);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_ng;
        topImage = R.drawable.map_node_in_ng;
        bottomImage = R.drawable.map_node_out_ng;
        color = Color.rgb(253, 180, 0);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter, Patch patch) {
        return new PatchNGPresenter(this, patchGraphPresenter, patch);
    }
}
