package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.KBPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCOPatch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchKBPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.text.DecimalFormat;

public class PatchKBMenuView extends PatchMenuView {

    public PatchKBMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void initializeModule(String title, View view) {

        KBPatch kbPatch = (KBPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        PianoView pianoView = (PianoView) view.findViewById(R.id.pianito);
        pianoView.setPresenter((PatchKBPresenter) patchPresenter);
        pianoView.setPatchId(patch.getId());

        createSeekBarComponent(R.id.seekBar_KB0, R.id.label_KB0, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, kbPatch.on_off, MenuScale.linear);

    }
}