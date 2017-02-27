package com.example.ddavi.synth3f.fragment;


import android.app.Fragment;
import android.app.Presentation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.evilduck.piano.views.instrument.PianoView;
import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.activity.MainActivity;
import com.example.ddavi.synth3f.presenters.PianoPresenter;

import org.w3c.dom.Text;

/**
 * Created by ddavi on 25/2/2017.
 */

public class FragmentOrgano extends Fragment {

    private PianoView piano;
    private PianoPresenter presenter;
    private TextView label_octava;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.presenter = new PianoPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_piano, container, false);

        //inicializo valores en teclas de piano
        piano = (PianoView) view.findViewById(R.id.pianito);
        presenter.initializePiano(piano);

        //inicializo valor de octava
        int value = presenter.getOctava();
        label_octava = (TextView) view.findViewById(R.id.labelOctava);
        label_octava.setText(String.valueOf(value));

        //Defino onCLick listener
        Button btn_more = (Button) view.findViewById(R.id.botonOctavaMas);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.incrementOctava();
                updateLabelOctava();
            }
        });

        Button btn_minus = (Button) view.findViewById(R.id.botonOctavaMenos);
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.reduceOctava();
                updateLabelOctava();
            }
        });

        return view;
    }

    private void updateLabelOctava(){
        piano.setINITIAL_OCTIVE(presenter.getOctava());
        label_octava.setText(String.valueOf(presenter.getOctava()));
    }

}
