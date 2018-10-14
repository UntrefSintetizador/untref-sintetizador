package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCAPatch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchVCAMenuView extends PatchMenuView {

    public PatchVCAMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        VCAPatch vcaPatch = (VCAPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarVCA1_0, R.id.labelVCA1_0, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, vcaPatch.on_off, MenuScale.LINEAR);
        createSeekBarComponent(R.id.seekBarVCA1_1, R.id.labelVCA1_1, "att_control", title, 100.0f, -100.0f, 1.0f, 1.0f, view, vcaPatch.att_control, MenuScale.EXPONENTIAL_CENTER);
        createSeekBarComponent(R.id.seekBarVCA1_2, R.id.labelVCA1_2, "base", title, 1.0f, 0.0f, 0.01f, 0.01f, view, vcaPatch.base, MenuScale.LINEAR);
        createSeekBarComponent(R.id.seekBarVCA1_3, R.id.labelVCA1_3, "clip", title, 1.0f, 0.0f, 1.0f, 1.0f, view, vcaPatch.clip, MenuScale.LINEAR);
    }

}
