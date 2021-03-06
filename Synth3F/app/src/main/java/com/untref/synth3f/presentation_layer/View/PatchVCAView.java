package com.untref.synth3f.presentation_layer.View;

import android.content.Context;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchVCAPresenter;

public class PatchVCAView extends PatchView {

    public PatchVCAView(Context context, WireDrawer wireDrawer,
                        PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context, wireDrawer, patchGraphPresenter, patch);
    }

    public PatchVCAView(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_vca;
        topImage = R.drawable.map_node_in_vca;
        bottomImage = R.drawable.map_node_out_vca;
        color = getResources().getColor(R.color.vca);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter,
                                             Patch patch) {
        return new PatchVCAPresenter(this, patchGraphPresenter, patch);
    }
}
