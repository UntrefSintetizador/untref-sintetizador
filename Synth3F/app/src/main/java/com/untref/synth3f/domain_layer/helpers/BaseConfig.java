package com.untref.synth3f.domain_layer.helpers;


import com.untref.synth3f.presentation_layer.activity.MainActivity;

/**
 * Created by oargueyo on 24/10/16.
 */

public abstract class BaseConfig {
    abstract public void stopAudio();
    abstract public void startAudio();
    abstract public void cleanup();
    abstract public void setContext(MainActivity c);
    abstract public boolean isServiceRunning();
    abstract public void evaluateMessage(String s);
    abstract public void resetPresets();
    abstract public void setPreset(String name);



}
