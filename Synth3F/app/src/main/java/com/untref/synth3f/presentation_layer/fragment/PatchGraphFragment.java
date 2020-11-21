package com.untref.synth3f.presentation_layer.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.ConfigFactory;
import com.untref.synth3f.domain_layer.helpers.Processor;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.presentation_layer.View.MapView;
import com.untref.synth3f.presentation_layer.View.OptionsMenuView;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.View.DragMenuView;
import com.untref.synth3f.presentation_layer.View.WireDrawer;
import com.untref.synth3f.presentation_layer.activity.StorageActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;

import java.util.HashMap;

public class PatchGraphFragment extends Fragment {

    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_OK = 1;
    public static final int REQUEST_LOAD = 1;
    public static final int REQUEST_SAVE = 2;
    private PatchGraphPresenter patchGraphPresenter;
    private View patchGraphView;
    private WireDrawer wireDrawer;
    private Processor processor;
    private MapView mapView;
    private Context context;
    private PatchMenuView patchMenuView;
    private boolean modeConnect;
    private OptionsMenuView optionsMenuView;
    private DragMenuView dragMenuView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.patchGraphPresenter = new PatchGraphPresenter(this, processor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        patchGraphView = inflater.inflate(R.layout.patch_graph_fragment, container,
                                         false);
        mapView = patchGraphView.findViewById(R.id.mapView);
        dragMenuView = patchGraphView.findViewById(R.id.drag_menu_view);
        dragMenuView.init();
        optionsMenuView = patchGraphView.findViewById(R.id.options_menu_view);
        optionsMenuView.init();
        createDragAndDropEvent();
        createSaveLoadEvent();
        createEngineEvent();
        createConnectDisconnectEvent();
        createOptionsMenuEvent();
        createDragMenuEvents();
        createNewPresetEvent();
        createWireDrawer(patchGraphView);
        patchMenuView = patchGraphView.findViewById(R.id.patch_menu_view);
        patchMenuView.setPatchGraphFragment(this);
        return patchGraphView;
    }

    public void setProcessor(Processor processor) {
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

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SAVE) {
            if (resultCode == RESULT_OK) {
                patchGraphPresenter.save(context,
                                         data.getStringExtra(getString(R.string.intent_filename)));
                boolean closeApp = data.getBooleanExtra(getString(R.string.intent_close_app), false);
                if (closeApp) {
                    getActivity().finishAffinity();
                }
                boolean removeAll = data.getBooleanExtra(getString(R.string.intent_remove_all), false);
                if (removeAll) {
                    removeAllPatches();
                }
            }
        }

        if (requestCode == REQUEST_LOAD) {
            if (resultCode == RESULT_OK) {
                loadFile(data.getStringExtra(getString(R.string.intent_filename)));
            }
        }
    }

    public PatchMenuView getPatchMenuView() {
        return patchMenuView;
    }

    public boolean isModeConnect() {
        return modeConnect;
    }

    public int findUnusedId() {
        int fID = 1;
        while (getActivity().findViewById(fID) != null) {
            fID++;
        }
        return fID;
    }

    public void handleBackPressedCallback() {
        int dialogStyle = R.style.Theme_AppCompat_Dialog_Alert;
        AlertDialog alertDialog = new AlertDialog.Builder(context, dialogStyle).create();
        alertDialog.setTitle(R.string.new_preset_dialog_title);
        alertDialog.setMessage(getResources().getString(R.string.close_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                getResources().getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.dialog_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finishAffinity();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startStorageActivity(REQUEST_SAVE, true, false);
                    }
                });
        alertDialog.show();
    }

    private void createWireDrawer(View view) {
        this.wireDrawer = new WireDrawer(context, mapView);
        wireDrawer.setId(findUnusedId());
        ConstraintLayout mapLayout = view.findViewById(R.id.map);
        mapLayout.addView(wireDrawer);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mapLayout);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.LEFT, R.id.map,
                              ConstraintSet.LEFT, 0);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.TOP, R.id.map,
                              ConstraintSet.TOP, 0);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.RIGHT, R.id.map,
                              ConstraintSet.RIGHT, 0);
        constraintSet.connect(wireDrawer.getId(), ConstraintSet.BOTTOM, R.id.map,
                              ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(mapLayout);

        ConstraintLayout.LayoutParams layoutParams =
                (ConstraintLayout.LayoutParams) wireDrawer.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;

        wireDrawer.bringToFront();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createDragAndDropEvent() {
        View.OnTouchListener listener = new DragAndDropListener();
        patchGraphView.findViewById(R.id.menuButtonDragVCO).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragVCA).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragVCF).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragEG).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragDAC).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragKB).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragLFO).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragMIX).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragNG).setOnTouchListener(listener);
        patchGraphView.findViewById(R.id.menuButtonDragSH).setOnTouchListener(listener);
    }

    private void createSaveLoadEvent() {
        patchGraphView.findViewById(R.id.menuSave).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startStorageActivity(REQUEST_SAVE, false, false);
                    }
                }
        );
        patchGraphView.findViewById(R.id.menuLoad).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startStorageActivity(REQUEST_LOAD, false, false);
                    }
                }
        );
    }

    private void startStorageActivity(int request, boolean closeApp, boolean removeAll) {
        Intent intent = new Intent(getActivity(), StorageActivity.class);
        intent.putExtra(getString(R.string.intent_mode), request);
        intent.putExtra(getString(R.string.intent_close_app), closeApp);
        intent.putExtra(getString(R.string.intent_remove_all), removeAll);
        startActivityForResult(intent, request);
    }

    private void createEngineEvent() {
        patchGraphView.findViewById(R.id.menuButtonChangeEngine).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        patchGraphPresenter.save(context, "_temp_");
                        ConfigFactory.changeEngine();
                        loadFile("_temp_");
                    }
                }
        );
    }

    private void createConnectDisconnectEvent() {
        modeConnect = true;
        patchGraphView.findViewById(R.id.menuConnect).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modeConnect = !modeConnect;
                        if (modeConnect) {
                            view.setBackgroundResource(R.drawable.menu_connect);
                        } else {
                            view.setBackgroundResource(R.drawable.menu_disconnect);
                        }
                    }
                }
        );
    }

    private void createOptionsMenuEvent() {
        patchGraphView.findViewById(R.id.menuButtonOpenOptionsMenu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (optionsMenuView.toggle()) {
                            view.setBackgroundResource(R.drawable.open_options_menu_on);
                        } else {
                            view.setBackgroundResource(R.drawable.open_options_menu_off);
                        }
                    }
                }
        );
    }

    private void createDragMenuEvents() {
        dragMenuView.findViewById(R.id.menuButtonScrollLeft).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dragMenuView.scrollLeft();
                    }
                }
        );
        dragMenuView.findViewById(R.id.menuButtonScrollRight).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dragMenuView.scrollRight();
                    }
                }
        );
        dragMenuView.findViewById(R.id.menuButtonOpenDragMenu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dragMenuView.toggle()) {
                            view.setBackgroundResource(R.drawable.open_drag_menu_on);
                        } else {
                            view.setBackgroundResource(R.drawable.open_drag_menu_off);
                        }
                    }
                }
        );
    }

    private void createNewPresetEvent() {
        View.OnClickListener listener = new NewPresetListener();
        patchGraphView.findViewById(R.id.menuNew).setOnClickListener(listener);
    }

    private void loadFile(String filename) {
        ConstraintLayout mapLayout = getActivity().findViewById(R.id.map);
        while (mapLayout.getChildCount() > 1) {
            mapLayout.removeViewAt(0);
        }

        PatchView[] patchViews = patchGraphPresenter.load(context, filename);
        HashMap<Integer, Integer> patchToView = new HashMap<>();

        DisplayMetrics displayMetrics = PatchGraphFragment.this.getResources().getDisplayMetrics();
        int hardcodedSize = (int) (displayMetrics.heightPixels / 12 / MapView.MAX_ZOOM);
        for (int i = 0; i < patchViews.length; i++) {
            PatchView patchView = patchViews[i];
            patchView.setId(findUnusedId());
            ConstraintLayout.LayoutParams drawerLayoutParams =
                    new ConstraintLayout.LayoutParams(hardcodedSize * patchView.widthRatio(),
                                                      hardcodedSize * 4);
            mapLayout.addView(patchView, drawerLayoutParams);

            int leftMargin = (int) (patchView.getPatch().getPosX() * displayMetrics.widthPixels) -
                             drawerLayoutParams.width / 2;
            int topMargin = (int) (patchView.getPatch().getPosY() * displayMetrics.heightPixels) -
                            drawerLayoutParams.height / 2;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mapLayout);
            constraintSet.connect(patchView.getId(), ConstraintSet.LEFT, R.id.map,
                                  ConstraintSet.LEFT, leftMargin);
            constraintSet.connect(patchView.getId(), ConstraintSet.TOP, R.id.map,
                                  ConstraintSet.TOP, topMargin);
            constraintSet.applyTo(mapLayout);

            patchToView.put(patchView.getPatchId(), i);
        }

        wireDrawer.clear();

        View start;
        View end;

        for (PatchView patchView : patchViews) {
            for (Connection connection : patchView.getPatch().getOutputConnections()) {
                processor.connect(connection);
                start = patchView.getOutputs()[connection.getSourceOutlet()];
                end = patchViews[patchToView.get(connection.getTargetPatch())].getInputs()[connection.getTargetInlet()];
                wireDrawer.startDraw(patchView, patchView.getColor());
                wireDrawer.addConnection(connection, start, end);
            }
        }

        wireDrawer.bringToFront();
        wireDrawer.reload(patchViews, displayMetrics.widthPixels, displayMetrics.heightPixels);
        wireDrawer.release();
    }

    private void removeAllPatches() {
        ConstraintLayout mapLayout = getActivity().findViewById(R.id.map);
        while (mapLayout.getChildCount() > 1) {
            mapLayout.removeViewAt(0);
        }
        wireDrawer.clear();
        patchGraphPresenter.deleteAll();
    }

    private class DragAndDropListener implements View.OnTouchListener {
        private int xDelta;
        private int yDelta;

        private PatchView patchView;

        private int hardcodedSize = 150;

        /**
         * Detecta los eventos realizados por el usuario sobre los patches
         *
         * @param view la vista donde se esta
         * @param event el evento detectado: ACTION_DOWN (toco la pantalla),
         *              ACTION_MOVE (mueve el dedo por la pantalla),
         *              ACTION_UP (dejo de tocar la pantalla)
         * @return false (si no hay un patchView), true en caso contrario
         */
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            view.performClick();

            DisplayMetrics displayMetrics =
                    PatchGraphFragment.this.getResources().getDisplayMetrics();
            hardcodedSize = (int) (displayMetrics.heightPixels / 12 / MapView.MAX_ZOOM);

            int x = (int) mapView.convertScreenToLayoutX(event.getRawX());
            int y = (int) mapView.convertScreenToLayoutY(event.getRawY());

            return checkMotionEvents(view, event, x, y);
        }

        private boolean checkMotionEvents(View view, MotionEvent event, int x, int y) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    patchView = patchGraphPresenter.createPatch((String) view.getTag());
                    if (patchView == null) {
                        return false;
                    }
                    doActionDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (patchView == null) {
                        return false;
                    }
                    patchView.movePatch(x, y, xDelta, yDelta);
                    break;
                case MotionEvent.ACTION_UP:
                    if (patchView == null) {
                        return false;
                    }
                    patchView.endMovePatch(x, y, event);
                    break;
            }
            return true;
        }

        private void doActionDown(int x, int y) {
            ConstraintLayout.LayoutParams drawerLayoutParams = new ConstraintLayout.LayoutParams(
                    hardcodedSize * patchView.widthRatio(),
                    hardcodedSize * 4
            );
            ConstraintLayout mapLayout = getActivity().findViewById(R.id.map);
            mapLayout.addView(patchView, drawerLayoutParams);

            xDelta = drawerLayoutParams.width / 2;
            yDelta = drawerLayoutParams.height / 2;

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mapLayout);
            constraintSet.connect(patchView.getId(), ConstraintSet.LEFT, R.id.map,
                                  ConstraintSet.LEFT, x - xDelta);
            constraintSet.connect(patchView.getId(), ConstraintSet.TOP, R.id.map,
                                  ConstraintSet.TOP, y - yDelta);
            constraintSet.applyTo(mapLayout);

            wireDrawer.bringToFront();
        }
    }

    private class NewPresetListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Resources resources = PatchGraphFragment.this.getResources();
            Context context = PatchGraphFragment.this.context;
            int dialogStyle = R.style.Theme_AppCompat_Dialog_Alert;
            final AlertDialog alertDialog = new AlertDialog.Builder(context, dialogStyle).create();
            alertDialog.setTitle(R.string.new_preset_dialog_title);
            alertDialog.setMessage(resources.getString(R.string.new_preset_dialog_message));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                    resources.getString(R.string.dialog_clean_screen),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            removeAllPatches();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                    resources.getString(R.string.dialog_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    resources.getString(R.string.dialog_save),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startStorageActivity(REQUEST_SAVE, false, true);
                        }
                    });
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    int red = getResources().getColor(R.color.red);
                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(red);
                }
            });
            alertDialog.show();
        }
    }
}
