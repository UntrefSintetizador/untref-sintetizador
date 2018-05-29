package com.untref.synth3f.presentation_layer.ModulesPopupWindow;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

/**
 * Created by ddavi on 2/10/2016.
 */

public class SHPopupWindow  extends ModulePopupWindow{

    public SHPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarSH1_1,R.id.labelSH1_1,"att_signal",title,1.0f,0.0f,0.01f,0.01f,view);
    }

}
