package com.example.ddavi.synth3f.helpers;


import com.example.ddavi.synth3f.activity.MainActivity;

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
