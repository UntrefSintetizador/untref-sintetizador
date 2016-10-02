package com.example.ddavi.prueba.Listeners;

import android.view.View;

import com.example.ddavi.prueba.ModulesPopupWindow.ModulePopupWindow;

/**
 * Created by ddavi on 2/10/2016.
 */

public class ModuleListener implements View.OnClickListener {

    private ModulePopupWindow popupWindow;

    public ModuleListener(){
    }

    public ModuleListener(ModulePopupWindow popup){
        popupWindow = popup;

    }

    @Override
    public void onClick(View v) {;
        v.setEnabled(false);
        popupWindow.showAsDropDown(popupWindow.getButton(), 150, -150);
    }

}