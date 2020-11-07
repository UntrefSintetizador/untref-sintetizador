package com.untref.synth3f.presentation_layer.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.widget.LinearLayout;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

/**
 * Es la vista de la cual extienden luego las vistas de los componentes (VCO, LFOL, DAC, EG, etc)
 */
public abstract class PatchView extends LinearLayout {

    protected PatchPresenter patchPresenter;
    protected WireDrawer wireDrawer;
    protected PatchGraphFragment patchGraphFragment;

    protected LinearLayout inputsLinearLayout;
    protected AppCompatImageView[] inputs;
    protected LinearLayout outputsLinearLayout;
    protected AppCompatImageView[] outputs;
    protected AppCompatImageView nodeImage;
    protected int centerImage;
    protected int topImage;
    protected int bottomImage;
    protected int color;

    private Patch patch;

    public PatchView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        super(context);
        patchPresenter = createPresenter(patchGraphPresenter, patch);
        patchGraphFragment = patchGraphPresenter.getPatchGraphFragment();
        this.patch = patch;
        initialize();
        drawPatch(context);
        this.wireDrawer = wireDrawer;
        createDragAndDropEvent();
        createLineEvents();
    }

    /**
     *
     * @return devuelve el color del componente
     */
    public int getColor() {
        return color;
    }

    /**
     *
     * @return las entradas que posee el componente, es decir las conexiones entrantes
     */
    public AppCompatImageView[] getInputs() {
        return inputs;
    }

    /**
     *
     * @return las salidas que posee el componente, es decir las conexiones que salen del componente
     */
    public AppCompatImageView[] getOutputs() {
        return outputs;
    }

    /**
     *
     * @return la id del patch
     */
    public int getPatchId() {
        return patch.getId();
    }

    /**
     *
     * @return el patch en cuestion
     */
    public Patch getPatch() {
        return patch;
    }

    /**
     *
     * @return
     */
    public int widthRatio() {
        return Math.max(Math.max(this.inputs.length, this.outputs.length), 2);
    }

    /**
     * Se ejecuta mientras se esta moviendo el patch
     *
     * @param x posicion en el eje horizontal donde se hace click
     * @param y posicion en el eje vertical donde se hace click
     * @param xDelta valor que ayuda a mantener el patch dentro de los margenes
     * @param yDelta valor que ayuda a mantener el patch dentro de los margenes
     */
    public void movePatch(int x, int y, int xDelta, int yDelta) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) PatchView.this.getLayoutParams();
        layoutParams.leftMargin = Math.min(x - xDelta,
                ((View) PatchView.this.getParent()).getWidth() - PatchView.this.getWidth());
        layoutParams.topMargin = Math.min(y - yDelta,
                ((View) PatchView.this.getParent()).getHeight() - PatchView.this.getHeight());
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        PatchView.this.setLayoutParams(layoutParams);
        wireDrawer.movePatch(patch, x, y);
    }

    /**
     * Se ejecuta cuando se suelta el patch (se lo deja de mover)
     *
     * @param x posicion en el eje horizontal donde se solto el patch
     * @param y posicion en el eje vertical donde se solto el patch
     * @param event el evento que haga el usuario
     */
    public void endMovePatch(float x, float y, MotionEvent event) {
        View delete = patchGraphFragment.getActivity().findViewById(R.id.menuDelete);
        Rect bounds = new Rect();
        int[] location = new int[2];
        delete.getLocationOnScreen(location);
        delete.getHitRect(bounds);
        DisplayMetrics displayMetrics = patchGraphFragment.getResources().getDisplayMetrics();
        patch.setPosX(x / displayMetrics.widthPixels);
        patch.setPosY(y / displayMetrics.heightPixels);
        if (bounds.contains((int) event.getRawX() - location[0] + bounds.left,
                (int) event.getRawY() - location[1] + bounds.top)) {
            patchPresenter.delete(patch.getId());
            ((ViewManager) PatchView.this.getParent()).removeView(PatchView.this);
        }
    }

    protected abstract PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter, Patch patch);

    /**
     * Inicializa aspectos visuales del componente (patch)
     */
    protected abstract void initialize();

    /**
     * Dibuja el patch
     *
     * @param context contexto en el que se dibuja el Patch
     */
    protected void drawPatch(Context context) {
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);

        nodeImage = new AppCompatImageView(context);
        nodeImage.setImageResource(centerImage);

        inputsLinearLayout = createLinearLayout(context);
        this.addView(nodeImage);
        outputsLinearLayout = createLinearLayout(context);

        LinearLayout.LayoutParams layoutParams;
        layoutParams = (LinearLayout.LayoutParams) nodeImage.getLayoutParams();
        layoutParams.height = 0;
        layoutParams.weight = 13;

        inputs = new AppCompatImageView[patch.getNumberOfInputs()];
        outputs = new AppCompatImageView[patch.getNumberOfOutputs()];
        populateConnectors(context, inputsLinearLayout, topImage, inputs);
        populateConnectors(context, outputsLinearLayout, bottomImage, outputs);
    }

    private LinearLayout createLinearLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.START);
        this.addView(linearLayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.height = 0;
        layoutParams.weight = 5;
        return linearLayout;
    }

    private void populateConnectors(Context context, LinearLayout linearLayout, int imageId, AppCompatImageView[] connectors) {
        AppCompatImageView connector;
        LinearLayout.LayoutParams layoutParams;

        for (int i = 0; i < connectors.length; i++) {
            connector = new AppCompatImageView(context);
            connector.setTag(i);
            connector.setImageResource(imageId);
            linearLayout.addView(connector);
            layoutParams = (LinearLayout.LayoutParams) connector.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.weight = 1;
            connector.setScaleType(AppCompatImageView.ScaleType.CENTER_INSIDE);
            connectors[i] = connector;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createDragAndDropEvent() {
        GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        };

        GestureDetector.OnDoubleTapListener onDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                PatchMenuView patchMenuView = patchGraphFragment.getPatchMenuView();
                patchMenuView.close();
                patchMenuView.setColor(getColor());
                if (patchPresenter.initMenuView(patchMenuView)) {
                    patchMenuView.open(patchPresenter);
                }
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        };

        final GestureDetector mDetector = new GestureDetector(patchGraphFragment.getActivity(), onGestureListener);
        mDetector.setOnDoubleTapListener(onDoubleTapListener);

        nodeImage.setOnTouchListener(
                new View.OnTouchListener() {

                    private int xDelta;
                    private int yDelta;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        if (mDetector.onTouchEvent(event)) {
                            return true;
                        }

                        int x = (int) patchGraphFragment.getMapView().convertScreenToLayoutX(event.getRawX());
                        int y = (int) patchGraphFragment.getMapView().convertScreenToLayoutY(event.getRawY());

                        ConstraintLayout.LayoutParams layoutParams;

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {

                            case MotionEvent.ACTION_DOWN:
                                layoutParams = (ConstraintLayout.LayoutParams) PatchView.this.getLayoutParams();
                                xDelta = x - layoutParams.leftMargin;
                                yDelta = y - layoutParams.topMargin;
                                break;

                            case MotionEvent.ACTION_MOVE:
                                movePatch(x, y, xDelta, yDelta);
                                break;
                            case MotionEvent.ACTION_UP:
                                endMovePatch(x, y, event);
                            default:
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createLineEvents() {
        View.OnTouchListener listener = new connectorListener(true);
        for (View view : inputs) {
            view.setOnTouchListener(listener);
        }
        listener = new connectorListener(false);
        for (View view : outputs) {
            view.setOnTouchListener(listener);
        }
    }

    private class connectorListener implements View.OnTouchListener {

        private boolean isInlet;

        private connectorListener(boolean isInlet) {
            this.isInlet = isInlet;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    if (patchGraphFragment.isModeConnect()) {
                        patchPresenter.setDragOn(PatchView.this.getPatchId(), view);
                        wireDrawer.startDraw(view, PatchView.this.getColor());
                    } else {
                        patchPresenter.disconnect(PatchView.this.getPatchId(), view, isInlet);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (patchGraphFragment.isModeConnect()) {
                        wireDrawer.draw(x, y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (patchGraphFragment.isModeConnect()) {
                        patchPresenter.setDragUp(x, y, isInlet);
                        wireDrawer.release();
                    }
                default:
                    break;
            }
            return true;
        }
    }
}
