package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.MIXPatch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchMIXMenuView extends PatchMenuView {

    public PatchMIXMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        MIXPatch mixPatch = (MIXPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarMIX1_0, R.id.labelMIX1_0, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, mixPatch.on_off, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarMIX1_1, R.id.labelMIX1_1, "ch1", title, 1.0f, 0.0f, 1.0f, 1.0f, view, mixPatch.ch1, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarMIX1_2, R.id.labelMIX1_2, "ch2", title, 1.0f, 0.0f, 1.0f, 1.0f, view, mixPatch.ch2, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarMIX1_3, R.id.labelMIX1_3, "ch3", title, 1.0f, 0.0f, 1.0f, 1.0f, view, mixPatch.ch3, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarMIX1_4, R.id.labelMIX1_4, "ch4", title, 1.0f, 0.0f, 1.0f, 1.0f, view, mixPatch.ch4, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarMIX1_5, R.id.labelMIX1_5, "master", title, 1.0f, 0.0f, 1.0f, 1.0f, view, mixPatch.master, MenuScale.linear);
    }

}
