package com.untref.synth3f.domain_layer.helpers;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;

import org.puredata.core.PdBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PureDataProcessor implements IProcessor {
    private final Handler handler = new Handler();
    private final PatchCollector patchCollector = new PatchCollector();
    private String mainPatchName = "pd-empty.pd";
    private HashMap<Integer, Integer> pureDataIDs;
    private List<Pair<String, Integer>> toDelete = new ArrayList<>();

    public PureDataProcessor() {
        pureDataIDs = new HashMap<>();
    }

    @Override
    public void sendValue(String name, Float value) {
        Log.i(name, Float.toString(value));
        PdBase.sendFloat(name, value);
    }

    @Override
    public void createPatch(String type, int patchId) {
        int newId = pureDataIDs.size();
        String name = "x_" + type + "_" + Integer.toString(patchId);
        pureDataIDs.put(patchId, newId);
        Object[] array = {10, 10, "x_" + type, name};
        Log.i("Create", mainPatchName + " " + "obj" + " 10 10 " + "x_" + type + " " + name);
        PdBase.sendMessage(mainPatchName, "obj", array);
    }

    @Override
    public void connect(Connection connection) {
        int newId = pureDataIDs.size();
        int sourcePatch = pureDataIDs.get(connection.getSourcePatch());
        int targetPatch = pureDataIDs.get(connection.getTargetPatch());
        String fadeName = "x_fade_" + Integer.toString(connection.getId());
        Object[] array = {10, 10, "x_fade", fadeName};
        pureDataIDs.put(connection.getId(), newId);
        PdBase.sendMessage(mainPatchName, "obj", array);
        Log.i("Create Fade", mainPatchName + " " + "obj" + " 10 10 x_fade " + fadeName);
        array = new Object[]{sourcePatch, connection.getSourceOutlet(), newId, 0};
        PdBase.sendMessage(mainPatchName, "connect", array);
        Log.i("Connect", mainPatchName + " " + "connect " + sourcePatch + " " + connection.getSourceOutlet() + " " + newId + " 0");
        array = new Object[]{newId, 0, targetPatch, connection.getTargetInlet()};
        PdBase.sendMessage(mainPatchName, "connect", array);
        Log.i("Connect", mainPatchName + " " + "connect " + newId + " 0 " + targetPatch + " " + connection.getTargetInlet());
        PdBase.sendFloat(fadeName, 1);
    }

    @Override
    public void delete(Patch patch) {

        for (Connection connection : patch.getOutputConnections()) {
            PdBase.sendFloat("x_fade_" + Integer.toString(connection.getId()), 0);
            toDelete.add(new Pair<>("x_fade_" + Integer.toString(connection.getId()), connection.getId()));
        }

        for (Connection connection : patch.getInputConnections()) {
            PdBase.sendFloat("x_fade_" + Integer.toString(connection.getId()), 0);
            toDelete.add(new Pair<>("x_fade_" + Integer.toString(connection.getId()), connection.getId()));
        }

        toDelete.add(new Pair<>("x_" + patch.getTypeName() + "_" + patch.getId(), patch.getId()));
        handler.postDelayed(patchCollector, 100);
    }

    @Override
    public void clear(Patch[] patches) {

        Object[] array = {"", 1};

        for (Patch patch : patches) {

            for (Connection connection : patch.getOutputConnections()) {
                array[0] = "x_fade_" + Integer.toString(connection.getId());
                PdBase.sendMessage(mainPatchName, "find", array);
                PdBase.sendMessage(mainPatchName, "cut", array);
            }

            for (Connection connection : patch.getInputConnections()) {
                array[0] = "x_fade_" + Integer.toString(connection.getId());
                PdBase.sendMessage(mainPatchName, "find", array);
                PdBase.sendMessage(mainPatchName, "cut", array);
            }

            array[0] = "x_" + patch.getTypeName() + "_" + patch.getId();
            PdBase.sendMessage(mainPatchName, "find", array);
            PdBase.sendMessage(mainPatchName, "cut", array);

        }

        toDelete.clear();
        pureDataIDs.clear();
    }

    @Override
    public void disconnect(Connection connection) {
        PdBase.sendFloat("x_fade_" + Integer.toString(connection.getId()), 0);
        toDelete.add(new Pair<>("x_fade_" + Integer.toString(connection.getId()), connection.getId()));
        handler.postDelayed(patchCollector, 100);
    }

    private class PatchCollector implements Runnable {

        @Override
        public void run() {
            Object[] array = {"", 1};

            for (Pair<String, Integer> patch : toDelete) {
                Integer position = pureDataIDs.remove(patch.second);

                for (Integer key : pureDataIDs.keySet()) {
                    Integer value = pureDataIDs.get(key);

                    if (value > position) {
                        pureDataIDs.put(key, value - 1);
                    }
                }

                array[0] = patch.first;
                PdBase.sendMessage(mainPatchName, "find", array);
                PdBase.sendMessage(mainPatchName, "cut", array);
            }

            toDelete.clear();
        }
    }
}
