package com.example.ddavi.synth3f.ModulesPopupWindow;

import android.view.View;
import android.widget.TextView;

import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.activity.MainActivity;
import com.example.ddavi.synth3f.fragment.FragmentMatriz;

/**
 * Created by ddavi on 2/10/2016.
 */

public class VCAPopupWindow extends ModulePopupWindow {

    public VCAPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarVCA1_1,R.id.labelVCA1_1,"att_control",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarVCA1_2,R.id.labelVCA1_2,"base",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarVCA1_3,R.id.labelVCA1_3,"clip",title,1.0f,0.0f,1.0f,1.0f,view);
    }

}
