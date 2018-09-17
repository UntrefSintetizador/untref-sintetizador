package com.untref.synth3f.domain_layer.helpers;

import android.content.Context;

import com.untref.synth3f.data_layer.FaustApi;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

import org.puredata.android.io.AudioParameters;

import java.util.List;

/**
 * Created by oargueyo on 31/10/16.
 */

public class FaustProcessor extends BaseProcessor {
    private long graphPointer;
    private Context context;

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
    }

    @Override
    public void setContext(MainActivity c) {
        context = c;
        AudioParameters.init(context);
        int channels = AudioParameters.suggestOutputChannels();
        int sample_rate = AudioParameters.suggestSampleRate();
        int buffer_size = AudioParameters.suggestOutputBufferSize(sample_rate);
        graphPointer = FaustApi.initFaustApi(channels, buffer_size, sample_rate);
    }

    @Override
    public boolean isServiceRunning() {
        return true;
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
    public void createPatch(String type, int patchId) {
        FaustApi.addPatchFaustApi(graphPointer, type, patchId);
    }

    @Override
    public void sendValue(String name, Float value) {
        FaustApi.setValueFaustApi(graphPointer, name, value);
    }

    @Override
    public void connect(Connection connection) {
        int source = connection.getSourcePatch();
        int outlet = connection.getSourceOutlet();
        int target = connection.getTargetPatch();
        int inlet = connection.getTargetInlet();
        FaustApi.connectFaustApi(graphPointer, source, outlet, target, inlet);
    }

    @Override
    public void delete(Patch patch) {
        List<Connection> patchOutputConnections = patch.getOutputConnections();

        for (Connection outputConnection : patchOutputConnections) {
            disconnectPatch(outputConnection);
        }

        FaustApi.removePatchFaustApi(graphPointer, patch.getId());
    }

    @Override
    public void clear(Patch[] patches) {

        for (Patch patch : patches) {
            delete(patch);
        }
    }

    private void disconnectPatch(Connection connection) {
        int source = connection.getSourcePatch();
        int outlet = connection.getSourceOutlet();
        int target = connection.getTargetPatch();
        int inlet = connection.getTargetInlet();
        FaustApi.disconnectFaustApi(graphPointer, source, outlet, target, inlet);
    }
}
