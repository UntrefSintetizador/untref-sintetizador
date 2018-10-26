package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.presentation_layer.activity.MainActivity;

public interface IConfig {
    void stopAudio();

    void startAudio();

    void cleanup();

    void setContext(MainActivity c);

    boolean isServiceRunning();

    IProcessor getProcessor();
}
