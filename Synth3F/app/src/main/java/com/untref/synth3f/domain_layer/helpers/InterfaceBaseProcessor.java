package com.untref.synth3f.domain_layer.helpers;


import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

/**
 * Created by oargueyo on 31/10/16.
 */

public interface InterfaceBaseProcessor {
    void stopAudio();

    void startAudio();

    void cleanup();

    void setContext(MainActivity c);

    boolean isServiceRunning();

    void evaluateMessage(String s);

    void resetPresets();

    void setPreset(String name, Float val);

    void sendValue(String name, Float value);

    void createPatch(String type, int patchId);

    void connect(Connection connection);

    void delete(Patch patch, String name);
}