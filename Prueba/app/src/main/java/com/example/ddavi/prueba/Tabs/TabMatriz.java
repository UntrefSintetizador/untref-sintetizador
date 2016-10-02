package com.example.ddavi.prueba.Tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ddavi.prueba.Listeners.VCOListener;
import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.MyGridView;
import com.example.ddavi.prueba.R;

import java.util.ArrayList;

/**
 * Created by ddavi on 13/9/2016.
 */
public class TabMatriz extends Fragment {

    private MyGridView matriz;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_matriz, container, false);
        matriz = (MyGridView) view.findViewById(R.id.grid_view);

        MainActivity activity = (MainActivity)getActivity();
        matriz.setAdapter(activity.getGridViewAdapter());
        matriz.setNumColumns(17);

        Button button_VC01 = (Button) view.findViewById(R.id.botonVCO1);
        button_VC01.setOnClickListener(new VCOListener((MainActivity)this.getActivity(),button_VC01,R.layout.popup_vco,"VCO1"));

        Button button_VC02 = (Button) view.findViewById(R.id.botonVCO2);
        button_VC02.setOnClickListener(new VCOListener((MainActivity)this.getActivity(),button_VC02,R.layout.popup_vco,"VCO2"));

        Button button_VC03 = (Button) view.findViewById(R.id.botonVCO3);
        button_VC03.setOnClickListener(new VCOListener((MainActivity)this.getActivity(),button_VC03,R.layout.popup_vco,"VCO3"));

        return view;
    }

}