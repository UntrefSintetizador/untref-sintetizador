package com.untref.synth3f.presentation_layer.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.MapView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.View.WireDrawer;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.R;

public class PatchGraphFragment extends Fragment {

    private PatchGraphPresenter patchGraphPresenter;
    private View PatchGraphView;
    private WireDrawer wireDrawer;
    private BaseProcessor processor;
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.patchGraphPresenter = new PatchGraphPresenter(this, processor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PatchGraphView = inflater.inflate(R.layout.patch_graph_fragment, container, false);
        mapView = (MapView) PatchGraphView.findViewById(R.id.mapView);
        createDragAndDropEvent();
        createWireDrawer(PatchGraphView);
        return PatchGraphView;
    }

    public void setProcessor(BaseProcessor processor) {
        this.processor = processor;
    }

    public WireDrawer getWireDrawer() {
        return wireDrawer;
    }

    public PatchGraphPresenter getPatchGraphPresenter() {
        return patchGraphPresenter;
    }

    public MapView getMapView() {
        return mapView;
    }

    public int findUnusedId() {
        int fID = 0;
        while (getActivity().findViewById(++fID) != null) ;
        return fID;
    }

    private void createWireDrawer(View view) {
        this.wireDrawer = new WireDrawer(getActivity(), patchGraphPresenter, mapView);
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
                        hardcodedSize = (int) (displayMetrics.heightPixels / 12 / MapView.MAX_ZOOM);

                        int x = (int) mapView.convertScreenToLayoutX(event.getRawX());
                        int y = (int) mapView.convertScreenToLayoutY(event.getRawY());

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

                                xDelta = drawerLayoutParams.width / 2;
                                yDelta = drawerLayoutParams.height / 2;

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(mapLayout);
                                constraintSet.connect(patchView.getId(), ConstraintSet.LEFT, R.id.map, ConstraintSet.LEFT, x - xDelta);
                                constraintSet.connect(patchView.getId(), ConstraintSet.TOP, R.id.map, ConstraintSet.TOP, y - yDelta);
                                constraintSet.applyTo(mapLayout);

                                wireDrawer.bringToFront();
                                break;
                            case MotionEvent.ACTION_UP:
                                if (patchView == null) {
                                    return false;
                                }
                                View delete = PatchGraphFragment.this.getActivity().findViewById(R.id.menuDelete);
                                Rect bounds = new Rect();
                                int[] location = new int[2];
                                delete.getLocationOnScreen(location);
                                delete.getHitRect(bounds);
                                if (bounds.contains((int) event.getRawX() - location[0] + bounds.left,
                                        (int) event.getRawY() - location[1] + bounds.top)) {
                                    Patch patch = patchGraphPresenter.delete(patchView.getPatchId());
                                    processor.delete(patch, view.getTag().toString().substring(2) + "_" + Integer.toString(patchView.getPatchId()));
                                    ((ViewManager) patchView.getParent()).removeView(patchView);
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (patchView == null) {
                                    return false;
                                }
                                ConstraintLayout.LayoutParams constraintLayoutParams = (ConstraintLayout.LayoutParams) patchView.getLayoutParams();
                                constraintLayoutParams.leftMargin = Math.min(x - xDelta,
                                        ((View) patchView.getParent()).getWidth() - patchView.getWidth());
                                constraintLayoutParams.topMargin = Math.min(y - yDelta,
                                        ((View) patchView.getParent()).getHeight() - patchView.getHeight());
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
