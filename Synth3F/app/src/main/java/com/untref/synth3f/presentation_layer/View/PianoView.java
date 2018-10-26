package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.util.AttributeSet;

import com.untref.synth3f.presentation_layer.presenters.PatchKBPresenter;

public class PianoView extends com.evilduck.piano.views.instrument.PianoView {

    private PatchKBPresenter presenter;
    private int patchId;

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPresenter(PatchKBPresenter presenter) {
        this.presenter = presenter;
    }

    public void setPatchId(int patchId) {
        this.patchId = patchId;
    }

    @Override
    protected void sendNote(int note) {
        presenter.setValue("x_kb_" + patchId + "_midi_note", note);
        presenter.setValue("x_kb_" + patchId + "_gate", 1);
    }

    @Override
    protected void releaseNote() {
        presenter.setValue("x_kb_" + patchId + "_gate", 0);
    }
}
