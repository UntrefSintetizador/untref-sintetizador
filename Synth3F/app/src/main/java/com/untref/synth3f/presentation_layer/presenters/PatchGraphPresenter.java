package com.untref.synth3f.presentation_layer.presenters;

import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.untref.synth3f.domain_layer.helpers.IProcessor;
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

    private final MapView mapView;
    private final WireDrawer wireDrawer;
    private final Context context;
    private IProcessor processor;
    private PatchGraphManager patchGraphManager;

    private int dragPatchOrigin;
    private View dragConnectorOrigin;

    public PatchGraphPresenter(IProcessor processor, MapView mapView, WireDrawer wireDrawer,
                               Context context) {
        this.processor = processor;
        this.mapView = mapView;
        this.wireDrawer = wireDrawer;
        this.context = context;
        this.patchGraphManager = new PatchGraphManager();
    }

    public IProcessor getProcessor() {
        return processor;
    }

    public PatchView createPatch(String type, PatchGraphFragment patchGraphFragment) {
        Patch patch;
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
                patch = new VCOPatch();
                break;
        }
        PatchView patchView = createPatchView(patch, patchGraphFragment);
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
        Rect bounds;
        int[] location;
        ConstraintLayout map = (ConstraintLayout) mapView.getChildAt(0);
        float scale = mapView.getScale();
        for (int i = 0; i < map.getChildCount() - 1; i++) {
            bounds = new Rect();
            PatchView patch = (PatchView) map.getChildAt(i);
            if (patch.getPatchId() != dragPatchOrigin) {
                AppCompatImageView[] targets;
                if (isInlet) {
                    targets = patch.getOutputs();
                } else {
                    targets = patch.getInputs();
                }
                for (AppCompatImageView targetConnector : targets) {
                    targetConnector.getHitRect(bounds);
                    location = getPositionOfView(targetConnector);
                    bounds.left *= scale;
                    bounds.right *= scale;
                    bounds.top *= scale;
                    bounds.bottom *= scale;
                    if (bounds.contains(x - location[0] + bounds.left, y - location[1] + bounds.top)) {
                        Connection connection;
                        if (isInlet) {
                            connection = patchGraphManager.connect(patch.getPatchId(), (int) targetConnector.getTag(), dragPatchOrigin, (int) dragConnectorOrigin.getTag());
                            wireDrawer.addConnection(connection, targetConnector, dragConnectorOrigin);
                        } else {
                            connection = patchGraphManager.connect(dragPatchOrigin, (int) dragConnectorOrigin.getTag(), patch.getPatchId(), (int) targetConnector.getTag());
                            wireDrawer.addConnection(connection, dragConnectorOrigin, targetConnector);
                        }
                        processor.connect(connection);
                    }
                }
            }
        }
    }

    public void disconnect(int patchId, View outlet, boolean isInlet) {
        Patch patch = patchGraphManager.getPatch(patchId);
        List<Connection> connectionList;
        int connectorId = (int) outlet.getTag();
        if (isInlet) {
            connectionList = patch.getInputConnections();
            for (int i = 0; i < connectionList.size(); i++) {
                Connection connection = connectionList.get(i);
                if (connection.getTargetInlet() == connectorId) {
                    patchGraphManager.disconnect(connection.getId());
                    wireDrawer.removeConnection(connection);
                    processor.disconnect(connection);
                    i--;
                }
            }
        } else {
            connectionList = patch.getOutputConnections();
            for (int i = 0; i < connectionList.size(); i++) {
                Connection connection = connectionList.get(i);
                if (connection.getSourceOutlet() == connectorId) {
                    patchGraphManager.disconnect(connection.getId());
                    wireDrawer.removeConnection(connection);
                    processor.disconnect(connection);
                    i--;
                }
            }
        }
    }

    public Patch delete(int patchId) {
        Patch patch = patchGraphManager.removePatch(patchId);
        wireDrawer.removePatch(patch);
        return patch;
    }

    public void deleteAll() {
        Patch[] patches = new Patch[this.patchGraphManager.getPatches().size()];
        this.patchGraphManager.getPatches().toArray(patches);
        processor.clear(patches);
        Patch[] patchesToRemove = patchGraphManager.removeAllPatches();
        for (Patch patch: patchesToRemove) {
            wireDrawer.removePatch(patch);
            this.processor.delete(patch);
        }
    }

    public void save(Context context, String filename) {
        JSONSerializer jsonSerializer = new JSONSerializer();
        jsonSerializer.save(context, patchGraphManager, filename);
    }

    public PatchView[] load(Context context, String filename,
                            PatchGraphFragment patchGraphFragment) {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Patch[] patches = new Patch[this.patchGraphManager.getPatches().size()];
        this.patchGraphManager.getPatches().toArray(patches);
        processor.clear(patches);

        this.patchGraphManager = jsonSerializer.load(context, filename);
        patches = new Patch[this.patchGraphManager.getPatches().size()];
        this.patchGraphManager.getPatches().toArray(patches);
        PatchView[] patchViews = new PatchView[patches.length];
        for (int i = 0; i < patches.length; i++) {
            Patch patch = patches[i];
            PatchView patchView = createPatchView(patch, patchGraphFragment);
            processor.createPatch(patch.getTypeName(), patch.getId());
            patch.initialize(processor);
            patchViews[i] = patchView;
        }
        return patchViews;
    }

    private int[] getPositionOfView(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;
    }

    private PatchView createPatchView(Patch patch, PatchGraphFragment patchGraphFragment) {
        PatchView patchView;
        switch (patch.getTypeName()) {
            case "vco":
                patchView = new PatchVCOView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
            case "vca":
                patchView = new PatchVCAView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
            case "vcf":
                patchView = new PatchVCFView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
            case "eg":
                patchView = new PatchEGView(context, wireDrawer, this,
                                            patchGraphFragment, patch);
                break;
            case "dac":
                patchView = new PatchDACView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
            case "kb":
                patchView = new PatchKBView(context, wireDrawer, this,
                                            patchGraphFragment, patch);
                break;
            case "lfo":
                patchView = new PatchLFOView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
            case "mix":
                patchView = new PatchMIXView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
            case "ng":
                patchView = new PatchNGView(context, wireDrawer, this,
                                            patchGraphFragment, patch);
                break;
            case "sh":
                patchView = new PatchSHView(context, wireDrawer, this,
                                            patchGraphFragment, patch);
                break;
            default:
                patchView = new PatchVCOView(context, wireDrawer, this,
                                             patchGraphFragment, patch);
                break;
        }
        return patchView;
    }
}
