package com.untref.synth3f.presentation_layer.presenters;

import android.widget.Button;

import com.untref.synth3f.presentation_layer.View.GridViewCustomAdapter;
import com.untref.synth3f.presentation_layer.View.MyGridView;
import com.untref.synth3f.presentation_layer.fragment.FragmentMatriz;

import java.util.HashMap;

/**
 * Created by ddavi on 25/2/2017.
 */

public class MatrizPresenter {

    private FragmentMatriz context;

    public MatrizPresenter(FragmentMatriz fragment){
        this.context = fragment;
    }


    public void setAdapterGridView(MyGridView grid){
        GridViewCustomAdapter gridViewAdapter = new GridViewCustomAdapter( context, new HashMap<String, Button>());
        grid.setAdapter(gridViewAdapter);
    }

}
