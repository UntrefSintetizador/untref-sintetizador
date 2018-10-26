package com.untref.synth3f.presentation_layer.presenters;

import com.evilduck.piano.views.instrument.PianoView;
import com.untref.synth3f.presentation_layer.fragment.FragmentOrgano;

/**
 * Created by ddavi on 25/2/2017.
 */

public class PianoPresenter {

    public static final int initial_octava = 5;
    private static final int MIN_OCTIVE = 5;
    private static final int MAX_OCTIVE = 8;

    private FragmentOrgano piano;
    private int octava;

    public PianoPresenter() {
        this.octava = initial_octava;
    }

    public int getOctava() {
        return octava;
    }

    public void reduceOctava() {
        if (octava > MIN_OCTIVE)
            octava--;
    }

    public void incrementOctava() {
        if (octava < MAX_OCTIVE)
            octava++;
    }

    public void initializePiano(PianoView piano) {

        piano.setOnKeyTouchListener(new PianoView.OnKeyTouchListener() {

            public void addNote(int midiCode) {
                //Note note = Note.fromCode(midiCode);
                //pianoView.addNotes(Arrays.asList(note));
            }

            @Override
            public void onTouch(int midiCode) {
                addNote(midiCode);
            }

            @Override
            public void onLongTouch(int midiCode) {
                addNote(midiCode);
            }
        });

    }
}
