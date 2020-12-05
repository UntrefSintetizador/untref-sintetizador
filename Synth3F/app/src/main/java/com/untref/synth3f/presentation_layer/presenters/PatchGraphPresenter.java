package com.untref.synth3f.presentation_layer.presenters;

import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.untref.synth3f.domain_layer.helpers.Processor;
import com.untref.synth3f.domain_layer.helpers.PatchGraphManager;
import com.untref.synth3f.domain_layer.serializers.JSONSerializer;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.EGPatch;
import com.untref.synth3f.entities.KBPatch;
import com.untref.synth3f.entities.LFOPatch;
import com.untref.synth3f.entities.MIXPatch;
import com.untref.synth3f.entities.NGPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.SHPatch;
import com.untref.synth3f.entities.VCAPatch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.entities.VCOPatch;
import com.untref.synth3f.presentation_layer.View.MapView;
import com.untref.synth3f.presentation_layer.View.PatchDACView;
import com.untref.synth3f.presentation_layer.View.PatchEGView;
import com.untref.synth3f.presentation_layer.View.PatchKBView;
import com.untref.synth3f.presentation_layer.View.PatchLFOView;
import com.untref.synth3f.presentation_layer.View.PatchMIXView;
import com.untref.synth3f.presentation_layer.View.PatchNGView;
import com.untref.synth3f.presentation_layer.View.PatchSHView;
import com.untref.synth3f.presentation_layer.View.PatchVCAView;
import com.untref.synth3f.presentation_layer.View.PatchVCFView;
import com.untref.synth3f.presentation_layer.View.PatchVCOView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.View.WireDrawer;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

import java.util.List;

public class PatchGraphPresenter {

    private PatchGraphFragment patchGraphFragment;
    private Processor processor;
    private PatchGraphManager patchGraphManager;

    private int dragPatchOrigin;
    private View dragConnectorOrigin;

    public PatchGraphPresenter(PatchGraphFragment patchGraphFragment, Processor processor) {
        this.patchGraphFragment = patchGraphFragment;
        this.processor = processor;
        this.patchGraphManager = new PatchGraphManager();
    }

    public Processor getProcessor() {
        return processor;
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public PatchView createPatch(String type) {
        Patch patch = createPatchEntity(type);
        PatchView patchView = createPatchView(patch);
        patchGraphManager.addPatch(patch);
        int newId = patchGraphFragment.findUnusedId();
        patchView.setId(newId);
        processor.createPatch(type, patch.getId());
        patch.initialize(processor);
        return patchView;
    }

    public void setDragOn(int patchId, View connector) {
        dragPatchOrigin = patchId;
        dragConnectorOrigin = connector;
    }

    public void tryConnect(int x, int y, boolean isInlet) {
        MapView mapView = patchGraphFragment.getMapView();
        ConstraintLayout map = (ConstraintLayout) mapView.getChildAt(0);
        float scale = mapView.getScale();
        for (int i = 0; i < map.getChildCount() - 1; i++) {
            PatchView patch = (PatchView) map.getChildAt(i);
            if (patch.getPatchId() != dragPatchOrigin) {
                AppCompatImageView[] targets;
                if (isInlet) {
                    targets = patch.getOutputs();
                } else {
                    targets = patch.getInputs();
                }
                tryConnectWithTarget(x, y, isInlet, scale, patch, targets);
            }
        }
    }

    public void disconnect(int patchId, View connector, boolean isInlet) {
        Patch patch = patchGraphManager.getPatch(patchId);
        List<Connection> connectionList;
        int connectorId = (int) connector.getTag();
        if (isInlet) {
            connectionList = patch.getInputConnections();
            for (int i = 0; i < connectionList.size(); i++) {
                Connection connection = connectionList.get(i);
                if (connection.getTargetInlet() == connectorId) {
                    disconnectTarget(connection);
                    i--;
                }
            }
        } else {
            connectionList = patch.getOutputConnections();
            for (int i = 0; i < connectionList.size(); i++) {
                Connection connection = connectionList.get(i);
                if (connection.getSourceOutlet() == connectorId) {
                    disconnectTarget(connection);
                    i--;
                }
            }
        }
    }

    public void delete(int patchId) {
        Patch patch = patchGraphManager.removePatch(patchId);
        patchGraphFragment.getWireDrawer().removePatch(patch);
    }

    public void deleteAll() {
        Patch[] patches = new Patch[patchGraphManager.getPatches().size()];
        patchGraphManager.getPatches().toArray(patches);
        processor.clear(patches);
        Patch[] patchesToRemove = patchGraphManager.removeAllPatches();
        for (Patch patch: patchesToRemove) {
            patchGraphFragment.getWireDrawer().removePatch(patch);
            processor.delete(patch);
        }
    }

    public void save(Context context, String filename) {
        JSONSerializer jsonSerializer = new JSONSerializer();
        jsonSerializer.save(context, patchGraphManager, filename);
    }

    public PatchView[] load(Context context, String filename) {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Patch[] patches = new Patch[patchGraphManager.getPatches().size()];
        patchGraphManager.getPatches().toArray(patches);
        processor.clear(patches);

        patchGraphManager = jsonSerializer.load(context, filename);
        patches = new Patch[patchGraphManager.getPatches().size()];
        patchGraphManager.getPatches().toArray(patches);
        PatchView[] patchViews = new PatchView[patches.length];
        for (int i = 0; i < patches.length; i++) {
            Patch patch = patches[i];
            PatchView patchView = createPatchView(patch);
            processor.createPatch(patch.getTypeName(), patch.getId());
            patch.initialize(processor);
            patchViews[i] = patchView;
        }
        return patchViews;
    }

    private Patch createPatchEntity(String type) {
        Patch patch = null;
        switch (type) {
            case "vco":
                patch = new VCOPatch();
                break;
            case "vca":
                patch = new VCAPatch();
                break;
            case "vcf":
                patch = new VCFPatch();
                break;
            case "eg":
                patch = new EGPatch();
                break;
            case "dac":
                patch = new DACPatch();
                break;
            case "kb":
                patch = new KBPatch();
                break;
            case "lfo":
                patch = new LFOPatch();
                break;
            case "mix":
                patch = new MIXPatch();
                break;
            case "ng":
                patch = new NGPatch();
                break;
            case "sh":
                patch = new SHPatch();
                break;
            default:
                break;
        }
        return patch;
    }

    private PatchView createPatchView(Patch patch) {
        PatchView patchView = null;
        Context context = patchGraphFragment.getActivity();
        WireDrawer wireDrawer = patchGraphFragment.getWireDrawer();
        PatchGraphPresenter patchGraphPresenter = patchGraphFragment.getPatchGraphPresenter();
        switch (patch.getTypeName()) {
            case "vco":
                patchView = new PatchVCOView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "vca":
                patchView = new PatchVCAView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "vcf":
                patchView = new PatchVCFView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "eg":
                patchView = new PatchEGView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "dac":
                patchView = new PatchDACView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "kb":
                patchView = new PatchKBView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "lfo":
                patchView = new PatchLFOView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "mix":
                patchView = new PatchMIXView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "ng":
                patchView = new PatchNGView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            case "sh":
                patchView = new PatchSHView(context, wireDrawer, patchGraphPresenter, patch);
                break;
            default:
                break;
        }
        return patchView;
    }

    private void tryConnectWithTarget(int x, int y, boolean isInlet, float scale, PatchView patch,
                                      AppCompatImageView[] targets) {
        int[] location;
        Rect bounds = new Rect();
        for (AppCompatImageView targetConnector : targets) {
            targetConnector.getHitRect(bounds);
            location = getPositionOfView(targetConnector);
            bounds.left *= scale;
            bounds.right *= scale;
            bounds.top *= scale;
            bounds.bottom *= scale;
            if (bounds.contains(x - location[0] + bounds.left,
                                y - location[1] + bounds.top)) {
                connectWithTarget(isInlet, patch, targetConnector);
            }
        }
    }

    private int[] getPositionOfView(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        WindowManager windowManager = patchGraphFragment.getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        display.getRealMetrics(realDisplayMetrics);
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        position[0] -= (realWidth - displayWidth) / 2;

        return position;
    }

    private void connectWithTarget(boolean isInlet, PatchView patch,
                                   AppCompatImageView targetConnector) {
        Connection connection;
        if (isInlet) {
            connection = patchGraphManager.connect(patch.getPatchId(),
                                                   (int) targetConnector.getTag(),
                                                   dragPatchOrigin,
                                                   (int) dragConnectorOrigin.getTag());
            patchGraphFragment.getWireDrawer().addConnection(connection, targetConnector,
                                                             dragConnectorOrigin);
        } else {
            connection = patchGraphManager.connect(dragPatchOrigin,
                                                   (int) dragConnectorOrigin.getTag(),
                                                   patch.getPatchId(),
                                                   (int) targetConnector.getTag());
            patchGraphFragment.getWireDrawer().addConnection(connection,
                                                             dragConnectorOrigin,
                                                             targetConnector);
        }
        processor.connect(connection);
    }

    private void disconnectTarget(Connection connection) {
        patchGraphManager.disconnect(connection.getId());
        patchGraphFragment.getWireDrawer().removeConnection(connection);
        processor.disconnect(connection);
    }
}
