package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.presenters.PatchKBPresenter;

public class PianoView extends com.evilduck.piano.views.instrument.PianoView {

    private static final int MIN_OCTIVE = 5;
    private static final int MAX_OCTIVE = 8;
    private PatchKBPresenter presenter;
    private int patchId;
    private TextView label_octava;
    private com.evilduck.piano.views.instrument.PianoView piano;
    private int octava = MIN_OCTIVE;

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void init(View view) {
        piano = findViewById(R.id.pianito);

        label_octava = view.findViewById(R.id.labelOctava);
        label_octava.setText(String.valueOf(octava));

        view.findViewById(R.id.botonOctavaMas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementOctava();
            }
        });

        view.findViewById(R.id.botonOctavaMenos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceOctava();
            }
        });
    }

    public void setPresenter(PatchKBPresenter presenter) {
        this.presenter = presenter;
    }

    public void setPatchId(int patchId) {
        this.patchId = patchId;
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

    private void reduceOctava() {
        if (octava > MIN_OCTIVE) {
            octava--;
            updateLabelOctava();
        }
    }

    private void incrementOctava() {
        if (octava < MAX_OCTIVE) {
            octava++;
            updateLabelOctava();
        }
    }

    private void updateLabelOctava() {
        piano.setINITIAL_OCTIVE(octava);
        label_octava.setText(String.valueOf(octava));
    }
}
