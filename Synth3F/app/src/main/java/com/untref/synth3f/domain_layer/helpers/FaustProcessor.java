package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.data_layer.FaustApi;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;

import java.util.List;

public class FaustProcessor implements IProcessor {

    private final long graphPointer;

    public FaustProcessor(long graphPointer) {
        this.graphPointer = graphPointer;
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
            disconnect(outputConnection);
        }

        FaustApi.removePatchFaustApi(graphPointer, patch.getId());
    }

    @Override
    public void clear(Patch[] patches) {

        for (Patch patch : patches) {
            delete(patch);
        }
    }

    @Override
    public void disconnect(Connection connection) {
        int source = connection.getSourcePatch();
        int outlet = connection.getSourceOutlet();
        int target = connection.getTargetPatch();
        int inlet = connection.getTargetInlet();
        FaustApi.disconnectFaustApi(graphPointer, source, outlet, target, inlet);
    }
}
