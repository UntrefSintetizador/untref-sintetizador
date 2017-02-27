package com.example.ddavi.synth3f.ModulesPopupWindow;

import android.view.View;
import android.widget.TextView;

import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.activity.MainActivity;
import com.example.ddavi.synth3f.fragment.FragmentMatriz;

/**
 * Created by ddavi on 2/10/2016.
 */

public class MIXPopupWindow extends ModulePopupWindow {

    public MIXPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarMIXER1_1,R.id.labelMIXER1_1,"ch1",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIXER1_2,R.id.labelMIXER1_2,"ch2",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIXER1_3,R.id.labelMIXER1_3,"ch3",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIXER1_4,R.id.labelMIXER1_4,"ch4",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIXER1_5,R.id.labelMIXER1_5,"master",title,1.0f,0.0f,0.1f,0.1f,view);
    }
}
