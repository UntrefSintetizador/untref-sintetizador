package com.untref.synth3f.domain_layer.helpers;


import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

/**
 * Created by oargueyo on 31/10/16.
 */

public class FaustProcessor extends BaseProcessor {
    @Override
    public void stopAudio() {

    }

    @Override
    public void startAudio() {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void setContext(MainActivity c) {

    }

    @Override
    public boolean isServiceRunning() {
        return false;
    }

    @Override
    public void evaluateMessage(String s) {

    }

    @Override
    public void resetPresets() {

    }

    @Override
    public void setPreset(String name, Float val) {

    }


    @Override
    public void createPatch(String name, int value) {

    }

    @Override
    public void sendValue(String name, Float value) {

    }

    @Override
    public void connect(Connection connection) {

    }

    @Override
    public void delete(Patch patch) {

    }

    @Override
    public void clear(Patch[] patches) {

    }
}
