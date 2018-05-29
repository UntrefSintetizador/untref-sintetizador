package com.untref.synth3f.presentation_layer.presenters;

import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.SparseArray;
import android.view.View;

import com.untref.synth3f.Connection;
import com.untref.synth3f.domain_layer.helpers.BaseProcessor;
import com.untref.synth3f.presentation_layer.View.PatchDACView;
import com.untref.synth3f.presentation_layer.View.PatchEGView;
import com.untref.synth3f.presentation_layer.View.PatchVCAView;
import com.untref.synth3f.presentation_layer.View.PatchVCFView;
import com.untref.synth3f.presentation_layer.View.PatchVCOView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

import java.util.ArrayList;
import java.util.List;


public class PatchGraphPresenter {

    private PatchGraphFragment patchGraphFragment;
    private SparseArray<PatchView> patches;
    private SparseArray<List<Connection>> connectionsSet;
    private BaseProcessor processor;

    private int dragPatchOrigin;
    private int dragOutlet;

    public PatchGraphPresenter(PatchGraphFragment patchGraphFragment) {
        this.patchGraphFragment = patchGraphFragment;
        this.patches = new SparseArray<>();
        this.connectionsSet = new SparseArray<>();
    }

    public void setProcessor(BaseProcessor processor) {
        this.processor = processor;
    }

    public BaseProcessor getProcessor() {
        return processor;
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public PatchView createPatch(String type) {
        PatchView patchView = null;
        switch (type) {
            case "x_vco":
                patchView = new PatchVCOView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter());
                break;
            case "x_vca":
                patchView = new PatchVCAView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter());
                break;
            case "x_vcf":
                patchView = new PatchVCFView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter());
                break;
            case "x_eg":
                patchView = new PatchEGView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter());
                break;
            case "x_dac":
                patchView = new PatchDACView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter());
                break;
//            case "x_kb_":
//                patchView = new MapItemViewKB(MainActivity.this, wireDrawer, maxPosition, newId);
//                break;
            default:
                patchView = new PatchVCOView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter());
                break;
        }
        int newId = patchGraphFragment.findUnusedId();
        patchView.setId(newId);
        patchView.setPatchId(newId);
        connectionsSet.put(newId, new ArrayList<Connection>());
        patches.put(newId, patchView);
        processor.createPatch(type, newId);
        return patchView;
    }

    public SparseArray<PatchView> getPatches() {
        return patches;
    }

    public SparseArray<List<Connection>> getConnections() {
        return connectionsSet;
    }

    public void setDragOn(int patchId, int outputId) {
        dragPatchOrigin = patchId;
        dragOutlet = outputId;
    }

    public void tryConnect(int x, int y) {
        Rect bounds;
        int[] location;
        for (int i = 0; i < patches.size(); i++) {
            bounds = new Rect();
            PatchView patch = patches.valueAt(i);
            if (patch.getPatchId() != dragPatchOrigin) {
                for (AppCompatImageView image : patch.getInputs()) {
                    image.getHitRect(bounds);
                    location = getPositionOfView(image);
                    if (bounds.contains(x - location[0] + bounds.left, y - location[1] + bounds.top)) {
                        Connection connection = new Connection(dragPatchOrigin, dragOutlet, patch.getPatchId(), (int) image.getTag());
                        connectionsSet.get(dragPatchOrigin).add(connection);
                        patchGraphFragment.getWireDrawer().invalidate();
                        processor.connect(dragPatchOrigin, dragOutlet, patch.getPatchId(), (int) image.getTag());
                    }
                }
            }
        }
    }

    public void delete(int patchId){
        connectionsSet.delete(patchId);
        for(int i = 0; i < connectionsSet.size(); i++){
            List<Connection> connections = connectionsSet.valueAt(i);
            for(int j = connections.size() - 1; j >= 0; j--){
                Connection connection = connections.get(j);
                if(connection.getEndPatch() == patchId){
                    connections.remove(j);
                }
            }
        }
    }

    private int[] getPositionOfView(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;
    }
}
