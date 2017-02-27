package com.example.ddavi.synth3f.presenters;

import android.widget.Button;
import com.example.ddavi.synth3f.View.GridViewCustomAdapter;
import com.example.ddavi.synth3f.View.MyGridView;
import com.example.ddavi.synth3f.fragment.FragmentMatriz;
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
