package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PatchGraphManager {

    private int maxId;
    private Map<Integer, Patch> patchMap;
    private Map<Integer, Connection> connectionMap;

    public PatchGraphManager() {
        this.maxId = 0;
        this.patchMap = new HashMap<>();
        this.connectionMap = new HashMap<>();
    }

    public Integer addPatch(Patch patch) {
        this.maxId += 1;
        patch.setId(maxId);
        patchMap.put(maxId, patch);
        return maxId;
    }

    public Patch removePatch(int patchId) {
        Patch patch = patchMap.remove(patchId);
        for (Connection connection : patch.getInputConnections()) {
            connectionMap.remove(connection.getId());
            patchMap.get(connection.getSourcePatch()).removeOutputConnection(connection);
        }
        for (Connection connection : patch.getOutputConnections()) {
            connectionMap.remove(connection.getId());
            patchMap.get(connection.getTargetPatch()).removeInputConnection(connection);
        }
        return patch;
    }

    public Connection connect(int sourcePatch, int sourceOutlet, int targetPatch, int targetInlet) {
        this.maxId += 1;
        Connection connection = new Connection();
        connection.setId(maxId);
        connection.setSourcePatch(sourcePatch);
        connection.setSourceOutlet(sourceOutlet);
        connection.setTargetPatch(targetPatch);
        connection.setTargetInlet(targetInlet);
        connectionMap.put(maxId, connection);
        patchMap.get(sourcePatch).addOutputConnection(connection);
        patchMap.get(targetPatch).addInputConnection(connection);
        return connection;
    }

    public Connection disconnect(int connectionId) {
        Connection connection = connectionMap.remove(connectionId);
        patchMap.get(connection.getSourcePatch()).removeOutputConnection(connection);
        patchMap.get(connection.getTargetPatch()).removeInputConnection(connection);
        return connection;
    }

    public Patch getPatch(int patchId) {
        return patchMap.get(patchId);
    }

    public Connection getConnection(int connectionId) {
        return connectionMap.get(connectionId);
    }

    public Collection<Patch> getPatches() {
        return patchMap.values();
    }

    public Collection<Connection> getConnections() {
        return connectionMap.values();
    }

}
