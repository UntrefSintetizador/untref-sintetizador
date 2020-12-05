package com.untref.synth3f.presentation_layer.View;

import android.content.Context;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchVCOPresenter;

public class PatchVCOView extends PatchView {

    public PatchVCOView(Context context, WireDrawer wireDrawer,
                        PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    public PatchVCOView(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_vco;
        topImage = R.drawable.map_node_in_vco;
        bottomImage = R.drawable.map_node_out_vco;
        color = getResources().getColor(R.color.vco);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter,
                                             Patch patch) {
        return new PatchVCOPresenter(this, patchGraphPresenter, patch);
    }
}
