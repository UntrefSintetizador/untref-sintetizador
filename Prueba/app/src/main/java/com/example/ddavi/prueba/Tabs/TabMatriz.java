package com.example.ddavi.prueba.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ddavi.prueba.MainActivity;
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

        MainActivity activity = (MainActivity)getActivity();
        View view;

        if (activity.getButtonsModulesMatriz().isEmpty())
            view = inflater.inflate(R.layout.menu_modulos, container, false);
        else {
            view = inflater.inflate(R.layout.tab_matriz, container, false);
            matriz = (MyGridView) view.findViewById(R.id.grid_view);
            matriz.setAdapter(activity.getGridViewAdapter());

            int cant_elements = activity.getButtonsModulesMatriz().size();
            int cant_columns = (cant_elements/4 > 15)? 15: (int)cant_elements/4;
            matriz.setNumColumns(cant_columns);
        }

        return view;
    }

}