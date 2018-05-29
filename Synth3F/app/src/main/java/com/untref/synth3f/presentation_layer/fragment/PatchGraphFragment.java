package com.untref.synth3f.presentation_layer.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.View.WireDrawer;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.R;

public class PatchGraphFragment extends Fragment {

    private PatchGraphPresenter patchGraphPresenter;
    private View PatchGraphView;
    private WireDrawer wireDrawer;
    private BaseProcessor processor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.patchGraphPresenter = new PatchGraphPresenter(this);
        this.patchGraphPresenter.setProcessor(processor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PatchGraphView = inflater.inflate(R.layout.patch_graph_fragment, container, false);
        createDragAndDropEvent();
        createWireDrawer(PatchGraphView);
        return PatchGraphView;
    }

    public void setProcessor(BaseProcessor processor){
        this.processor = processor;
    }

    public WireDrawer getWireDrawer() {
        return wireDrawer;
    }

    public PatchGraphPresenter getPatchGraphPresenter() {
        return patchGraphPresenter;
    }

    public int findUnusedId() {
        int fID = 0;
        while (getActivity().findViewById(++fID) != null) ;
        return fID;
    }

    private void createWireDrawer(View view) {
        this.wireDrawer = new WireDrawer(getActivity(), patchGraphPresenter);
        wireDrawer.setId(findUnusedId());

        ConstraintLayout mapLayout = (ConstraintLayout) view.findViewById(R.id.map);
        mapLayout.addView(wireDrawer);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mapLayout);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.LEFT, R.id.map, ConstraintSet.LEFT, 0);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.TOP, R.id.map, ConstraintSet.TOP, 0);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.RIGHT, R.id.map, ConstraintSet.RIGHT, 0);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.BOTTOM, R.id.map, ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(mapLayout);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) wireDrawer.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;

        wireDrawer.bringToFront();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createDragAndDropEvent() {
        View.OnTouchListener listener =
                new View.OnTouchListener() {

                    private int xDelta;
                    private int yDelta;

                    private PatchView patchView;

                    private int hardcodedSize = 150;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        DisplayMetrics displayMetrics = PatchGraphFragment.this.getResources().getDisplayMetrics();
                        hardcodedSize = displayMetrics.heightPixels / 12;

                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                patchView = patchGraphPresenter.createPatch((String) view.getTag());
                                if (patchView == null) {
                                    return false;
                                }
                                int connectors = patchView.widthRatio();
                                DrawerLayout.LayoutParams drawerLayoutParams = new DrawerLayout.LayoutParams(hardcodedSize * connectors, hardcodedSize * 4);

                                ConstraintLayout mapLayout = (ConstraintLayout) getActivity().findViewById(R.id.map);
                                mapLayout.addView(patchView, drawerLayoutParams);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(mapLayout);
                                constraintSet.connect(patchView.getId(), ConstraintSet.LEFT, R.id.map, ConstraintSet.LEFT, x - drawerLayoutParams.width / 2);
                                constraintSet.connect(patchView.getId(), ConstraintSet.TOP, R.id.map, ConstraintSet.TOP, y - drawerLayoutParams.height / 2);
                                constraintSet.applyTo(mapLayout);

                                wireDrawer.bringToFront();

                                xDelta = hardcodedSize * patchView.widthRatio() / 2;
                                yDelta = hardcodedSize / 2;
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (patchView == null) {
                                    return false;
                                }
                                ConstraintLayout.LayoutParams constraintLayoutParams = (ConstraintLayout.LayoutParams) patchView.getLayoutParams();
                                constraintLayoutParams.leftMargin = x - xDelta;
                                constraintLayoutParams.topMargin = y - yDelta;
                                constraintLayoutParams.rightMargin = 0;
                                constraintLayoutParams.bottomMargin = 0;
                                patchView.setLayoutParams(constraintLayoutParams);
                            default:
                                break;
                        }
                        return true;
                    }
                };
        PatchGraphView.findViewById(R.id.menuButtonDragVCO).setOnTouchListener(listener);
        PatchGraphView.findViewById(R.id.menuButtonDragVCA).setOnTouchListener(listener);
        PatchGraphView.findViewById(R.id.menuButtonDragVCF).setOnTouchListener(listener);
        PatchGraphView.findViewById(R.id.menuButtonDragEG).setOnTouchListener(listener);
        PatchGraphView.findViewById(R.id.menuButtonDragDAC).setOnTouchListener(listener);
    }
}
