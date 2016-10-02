package com.example.ddavi.prueba.Tabs;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evilduck.piano.music.Note;
import com.evilduck.piano.views.instrument.PianoView;
import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

import java.util.Arrays;

/**
 * Created by ddavi on 13/9/2016.
 */
public class TabPiano extends Fragment {

    private PianoView pianoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.tab_piano, container, false);
        int value = ((MainActivity)this.getActivity()).getOctava();

        //inicializo valores en teclas de piano
        pianoView = (PianoView) view.findViewById(R.id.pianito);
        //initializePiano();

        //inicializo valor de octava

        TextView label = (TextView)view.findViewById(R.id.labelOctava);
        label.setText(String.valueOf(value));

        return view;
    }

    public void initializePiano(){

        pianoView.setOnKeyTouchListener(new PianoView.OnKeyTouchListener() {

            public void addNote(int midiCode){
                Note note = Note.fromCode(midiCode);
                pianoView.addNotes(Arrays.asList(note));
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