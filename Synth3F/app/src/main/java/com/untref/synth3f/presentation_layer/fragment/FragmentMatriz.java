package com.untref.synth3f.presentation_layer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.untref.synth3f.presentation_layer.View.MyGridView;
import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.presenters.MatrizPresenter;

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
