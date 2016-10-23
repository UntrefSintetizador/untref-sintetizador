package com.example.ddavi.prueba.ModulesPopupWindow;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

/**
 * Created by ddavi on 2/10/2016.
 */

public class EGPopupWindow extends ModulePopupWindow {

    public EGPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarEG1_1,R.id.labelEG1_1,"attack",title,5000.0f,0.0f,1.0f,1.0f,view);
        createSeekBarComponent(R.id.seekBarEG1_2,R.id.labelEG1_2,"decay",title,5000.0f,0.0f,1.0f,1.0f,view);
        createSeekBarComponent(R.id.seekBarEG1_3,R.id.labelEG1_3,"sustain",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarEG1_4,R.id.labelEG1_4,"release",title,5000.0f,0.0f,1.0f,1.0f,view);
        createSeekBarComponent(R.id.seekBarEG1_5,R.id.labelEG1_5,"gate",title,1.0f,0.0f,1.0f,1.0f,view);
    }

}
