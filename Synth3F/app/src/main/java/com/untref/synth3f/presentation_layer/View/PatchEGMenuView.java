package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

/**
 * Created by ddavi on 2/10/2016.
 */

public class PatchEGMenuView extends PatchMenuView {

    public PatchEGMenuView(MainActivity container, int layout, String name, PatchPresenter patchPresenter){
        super(container,layout,name, patchPresenter);
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
