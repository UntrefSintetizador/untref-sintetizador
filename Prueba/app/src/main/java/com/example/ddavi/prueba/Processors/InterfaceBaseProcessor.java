package com.example.ddavi.prueba.Processors;

import com.example.ddavi.prueba.MainActivity;

/**
 * Created by oargueyo on 31/10/16.
 */

public interface InterfaceBaseProcessor {
    public void stopAudio();
    public void startAudio();
    public void cleanup();
    public void setContext(MainActivity c);
    public boolean isServiceRunning();
    public void evaluateMessage(String s);
    public void resetPresets();
    public void setPreset(String name , Float val);
    public void sendValue(String name , Float value);
}
