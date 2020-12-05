package com.untref.synth3f.presentation_layer.View;

import android.content.Context;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchMIXPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchMIXView extends PatchView {

    public PatchMIXView(Context context, WireDrawer wireDrawer,
                        PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    public PatchMIXView(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_mix;
        topImage = R.drawable.map_node_in_mix;
        bottomImage = R.drawable.map_node_out_mix;
        color = getResources().getColor(R.color.mix);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter,
                                             Patch patch) {
        return new PatchMIXPresenter(this, patchGraphPresenter, patch);
    }
}
