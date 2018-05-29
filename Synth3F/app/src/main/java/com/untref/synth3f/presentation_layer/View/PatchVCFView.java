package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchVCFPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchVCOPresenter;

public class PatchVCFView extends PatchView {

    public PatchVCFView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter) {
        super(context, wireDrawer, patchGraphPresenter);
    }

    @Override
    protected void initialize() {
        centerImage = R.drawable.map_node_vcf;
        topImage = R.drawable.map_node_in_vcf;
        bottomImage = R.drawable.map_node_out_vcf;
        color = Color.rgb(253,180 ,00);
    }

    @Override
    protected PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter) {
        return new PatchVCFPresenter(this, patchGraphPresenter);
    }
}
