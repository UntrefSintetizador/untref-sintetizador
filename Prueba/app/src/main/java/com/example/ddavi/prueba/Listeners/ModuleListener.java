package com.example.ddavi.prueba.Listeners;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.ddavi.prueba.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.prueba.MyGridView.GridViewCustomAdapter;

import org.puredata.core.PdBase;

/**
 * Created by ddavi on 2/10/2016.
 */

public class ModuleListener implements View.OnClickListener {

    private GridViewCustomAdapter gridViewAdapter;

    public ModuleListener(){
        gridViewAdapter = null;

    }

    public ModuleListener(GridViewCustomAdapter adapter){
        gridViewAdapter = adapter;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        String msg = "connect-"+ button.getText();

        if (button.getCurrentTextColor() == Color.BLACK) {
            Float value = 1.0f;
            PdBase.sendFloat(msg, value);
            button.setBackgroundColor(Color.parseColor("#FF9800"));
            Log.i("MSJ A PD ", msg);
            button.setTextColor(Color.WHITE);
            button.setWidth(80);
            gridViewAdapter.getItemsPressed().add(button);
            //v.setEnabled(false);
            //popupWindow.showAsDropDown(popupWindow.getButton(), 150,-200);

        } else {
            Float value = 0.0f;
            PdBase.sendFloat(msg, value);
            button.setBackgroundColor(Color.parseColor("#607D8B"));
            Log.i("MSJ A PD ", msg);
            button.setTextColor(Color.BLACK);
            button.setWidth(81);
            gridViewAdapter.getItemsPressed().remove(gridViewAdapter.getItemsPressed().indexOf(button));

        }
    }

}