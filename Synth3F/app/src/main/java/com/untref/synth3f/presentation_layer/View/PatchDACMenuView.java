package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

public class PatchDACMenuView extends PatchMenuView {

    public PatchDACMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        DACPatch dacPatch = (DACPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBar_DAC0, R.id.label_DAC0, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, dacPatch.on_off, MenuScale.LINEAR);
    }

}