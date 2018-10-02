package com.untref.synth3f.presentation_layer.ModulesPopupWindow;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

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

        createSeekBarComponent(R.id.seekBarMIX1_1,R.id.labelMIX1_1,"ch1",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIX1_2,R.id.labelMIX1_2,"ch2",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIX1_3,R.id.labelMIX1_3,"ch3",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIX1_4,R.id.labelMIX1_4,"ch4",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBarMIX1_5,R.id.labelMIX1_5,"master",title,1.0f,0.0f,0.1f,0.1f,view);
    }
}
