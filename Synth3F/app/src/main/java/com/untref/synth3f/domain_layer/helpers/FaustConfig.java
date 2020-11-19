package com.untref.synth3f.domain_layer.helpers;

import android.content.Context;

import com.untref.synth3f.data_layer.FaustApi;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

import org.puredata.android.io.AudioParameters;

public class FaustConfig implements Config {
    private Context context;
    private long graphPointer;
    private FaustProcessor processor;

    @Override
    public void stopAudio() {
        FaustApi.stopAudioFaustApi(graphPointer);
    }

    @Override
    public void startAudio() {
        FaustApi.startAudioFaustApi(graphPointer);
    }

    @Override
    public void cleanup() {
        FaustApi.disposeFaustApi(graphPointer);
        graphPointer = 0;
    }

    @Override
    public void setContext(MainActivity context) {
        this.context = context;
        AudioParameters.init(this.context);
        int channels = AudioParameters.suggestOutputChannels();
        int sample_rate = AudioParameters.suggestSampleRate();
        int buffer_size = AudioParameters.suggestOutputBufferSize(sample_rate);
        graphPointer = FaustApi.initFaustApi(channels, buffer_size, sample_rate);
        processor = new FaustProcessor(graphPointer);
    }

    @Override
    public boolean isServiceRunning() {
        return graphPointer != 0;
    }

    @Override
    public Processor getProcessor() {
        return processor;
    }
}
