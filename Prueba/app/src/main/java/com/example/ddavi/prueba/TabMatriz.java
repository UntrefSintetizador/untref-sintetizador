package com.example.ddavi.prueba;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
/**
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity)getActivity();

        if (activity.getMatriz_modulos() == null) {
            activity.setMatriz(matriz);
            activity.setTamanioGrilla();
        }else
            matriz = activity.getMatriz_modulos();
    }
**/
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_matriz, container, false);
        matriz = (MyGridView) view.findViewById(R.id.grid_view);

        MainActivity activity = (MainActivity)getActivity();
        matriz.setAdapter(activity.getGridViewAdapter());
        matriz.setNumColumns(17);

        return view;
    }

}