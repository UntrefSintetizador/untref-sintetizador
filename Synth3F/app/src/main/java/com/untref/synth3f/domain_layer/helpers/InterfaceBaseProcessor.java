package com.untref.synth3f.domain_layer.helpers;


import com.untref.synth3f.presentation_layer.activity.MainActivity;

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
    public void setPreset(String name, Float val);
    public void sendValue(String name, Float value);
    public void createPatch(String type, int patchId);
    public void connect(Integer origin, Integer outlet, Integer destine, Integer inlet);
    public void delete(Integer patchId, String name);
}
