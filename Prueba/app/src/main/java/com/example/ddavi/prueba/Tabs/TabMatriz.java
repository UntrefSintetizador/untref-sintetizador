package com.example.ddavi.prueba.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.ddavi.prueba.Listeners.ModuleListener;
import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.prueba.MyGridView.MyGridView;
import com.example.ddavi.prueba.R;

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

        ModulePopupWindow popupwindow;

        Button button_VCO1 = (Button) view.findViewById(R.id.botonVCO1);
        popupwindow = activity.getVco1Window();
        popupwindow.setButton(button_VCO1);
        //new VCOPopupWindow((MainActivity)this.getActivity(),button_VCO1,R.layout.popup_vco,"VCO1");
        button_VCO1.setOnClickListener(new ModuleListener(popupwindow));

        Button button_VCO2 = (Button) view.findViewById(R.id.botonVCO2);
        popupwindow = activity.getVco2Window();
        popupwindow.setButton(button_VCO2);
        button_VCO2.setOnClickListener(new ModuleListener(popupwindow));

        Button button_VCO3 = (Button) view.findViewById(R.id.botonVCO3);
        popupwindow = activity.getVco3Window();
        popupwindow.setButton(button_VCO3);
        button_VCO3.setOnClickListener(new ModuleListener(popupwindow));

        Button button_VCA1 = (Button) view.findViewById(R.id.botonVCA1);
        popupwindow = activity.getVca1Window();
        popupwindow.setButton(button_VCA1);
        button_VCA1.setOnClickListener(new ModuleListener(popupwindow));

        Button button_VCA2 = (Button) view.findViewById(R.id.botonVCA2);
        popupwindow = activity.getVca2Window();
        popupwindow.setButton(button_VCA2);
        button_VCA2.setOnClickListener(new ModuleListener(popupwindow));

        Button botonMIXER1 = (Button) view.findViewById(R.id.botonMIXER1);
        popupwindow = activity.getMixWindow();
        popupwindow.setButton(botonMIXER1);
        botonMIXER1.setOnClickListener(new ModuleListener(popupwindow));

        Button botonVCF1 = (Button) view.findViewById(R.id.botonVCF1);
        popupwindow = activity.getVcf1Window();
        popupwindow.setButton(botonVCF1);
        botonVCF1.setOnClickListener(new ModuleListener(popupwindow));

        Button botonVCF2 = (Button) view.findViewById(R.id.botonVCF2);
        popupwindow = activity.getVcf2Window();
        popupwindow.setButton(botonVCF2);
        botonVCF2.setOnClickListener(new ModuleListener(popupwindow));

        Button botonEG1 = (Button) view.findViewById(R.id.botonEG1);
        popupwindow = activity.getEg1Window();
        popupwindow.setButton(botonEG1);
        botonEG1.setOnClickListener(new ModuleListener(popupwindow));

        Button botonEG2 = (Button) view.findViewById(R.id.botonEG2);
        popupwindow = activity.getEg2Window();
        popupwindow.setButton(botonEG2);
        botonEG2.setOnClickListener(new ModuleListener(popupwindow));

        Button botonSH1 = (Button) view.findViewById(R.id.botonSH1);
        popupwindow = activity.getShWindow();
        popupwindow.setButton(botonSH1);
        botonSH1.setOnClickListener(new ModuleListener(popupwindow));

        return view;
    }

}