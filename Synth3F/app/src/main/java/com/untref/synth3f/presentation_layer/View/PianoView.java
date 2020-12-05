package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.KBPatch;
import com.untref.synth3f.presentation_layer.presenters.PatchKBPresenter;

public class PianoView extends com.evilduck.piano.views.instrument.PianoView {

    private static final int MIN_OCTAVE = 0;
    private static final int MAX_OCTAVE = 8;
    private PatchKBPresenter presenter;
    private TextView octaveLabel;
    // TODO: replace uses of the attribute defined here by uses of this class
    private com.evilduck.piano.views.instrument.PianoView piano;
    private KBPatch patch;

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(View view, KBPatch patch) {
        this.patch = patch;
        piano = findViewById(R.id.pianito);

        octaveLabel = view.findViewById(R.id.label_octave);
        octaveLabel.setText(String.valueOf(patch.octave));
        updateOctaveLabel();

        view.findViewById(R.id.button_octave_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementOctave();
            }
        });

        view.findViewById(R.id.button_octave_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceOctave();
            }
        });
    }

    public void setPresenter(PatchKBPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void sendNote(int note) {
        presenter.setValue("midi_note", note);
        presenter.setValue("gate", 1);
    }

    @Override
    protected void releaseNote() {
        presenter.setValue("gate", 0);
    }

    private void reduceOctave() {
        if (patch.octave > MIN_OCTAVE) {
            patch.octave--;
            updateOctaveLabel();
        }
    }

    private void incrementOctave() {
        if (patch.octave < MAX_OCTAVE) {
            patch.octave++;
            updateOctaveLabel();
        }
    }

    private void updateOctaveLabel() {
        piano.setINITIAL_OCTIVE(patch.octave);
        octaveLabel.setText(String.valueOf(patch.octave));
    }
}
