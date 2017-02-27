package com.example.ddavi.synth3f.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ddavi.synth3f.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.View.MyGridView;
import com.example.ddavi.synth3f.activity.MainActivity;
import com.example.ddavi.synth3f.presenters.MatrizPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ddavi on 25/2/2017.
 */

public class FragmentMatriz extends Fragment {

    private MyGridView matriz;
    private MatrizPresenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.presenter = new MatrizPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

            view = inflater.inflate(R.layout.fragment_matriz, container, false);
            matriz = (MyGridView) view.findViewById(R.id.grid_view);
            presenter.setAdapterGridView(matriz);

            //int cant_elements = activity.getButtonsModulesMatriz().size();
            //int cant_columns = (cant_elements/4 > 15)? 15: (int)cant_elements/4;
            matriz.setNumColumns(3);

        return view;
    }

}
