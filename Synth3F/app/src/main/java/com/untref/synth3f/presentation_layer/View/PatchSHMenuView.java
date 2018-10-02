package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.SHPatch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchSHMenuView extends PatchMenuView {

    public PatchSHMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        SHPatch shPatch = (SHPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarSH1_1, R.id.labelSH1_1, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, shPatch.on_off, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarSH1_2, R.id.labelSH1_2, "att_signal", title, 100.0f, -100.0f, 1.0f, 1.0f, view, shPatch.att_signal, MenuScale.exponential_center);
        createSeekBarComponent(R.id.seekBarSH1_3, R.id.labelSH1_3, "glide", title, 5000.0f, 0.0f, 0.01f, 0.01f, view, shPatch.glide, MenuScale.exponential_left);
    }

}
