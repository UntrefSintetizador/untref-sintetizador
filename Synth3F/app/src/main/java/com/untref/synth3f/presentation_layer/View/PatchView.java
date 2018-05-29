package com.untref.synth3f.presentation_layer.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.widget.LinearLayout;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

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

    protected int patchId;

    public PatchView(Context context, WireDrawer wireDrawer, PatchGraphPresenter patchGraphPresenter) {
        super(context);
        patchPresenter = createPresenter(patchGraphPresenter);
        patchGraphFragment = patchGraphPresenter.getPatchGraphFragment();
        initialize();
        drawPatch(context);
        this.wireDrawer = wireDrawer;
        createDragAndDropEvent();
        createLineEvents();
    }

    public int getColor() {
        return color;
    }

    public AppCompatImageView[] getInputs() {
        return inputs;
    }

    public AppCompatImageView[] getOutputs() {
        return outputs;
    }

    public void setPatchId(int patchId) {
        this.patchId = patchId;
    }

    public int getPatchId() {
        return patchId;
    }

    public int widthRatio() {
        return Math.max(Math.max(this.inputs.length, this.outputs.length), 2);
    }

    protected abstract PatchPresenter createPresenter(PatchGraphPresenter patchGraphPresenter);

    protected abstract void initialize();

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

        inputs = new AppCompatImageView[patchPresenter.getNumberOfInputs()];
        outputs = new AppCompatImageView[patchPresenter.getNumberOfOutputs()];
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
                PatchMenuView patchMenuView = patchPresenter.createMenuView(
                        (MainActivity) patchGraphFragment.getActivity());
                patchMenuView.showAsDropDown(nodeImage, 150, -500);
                patchMenuView.setButton(nodeImage);
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        };

        final GestureDetectorCompat mDetector = new GestureDetectorCompat(patchGraphFragment.getActivity(), onGestureListener);
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

                        final int x = (int) event.getRawX();
                        final int y = (int) event.getRawY();

                        ConstraintLayout.LayoutParams layoutParams;

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {

                            case MotionEvent.ACTION_DOWN:
                                layoutParams = (ConstraintLayout.LayoutParams) PatchView.this.getLayoutParams();
                                xDelta = x - layoutParams.leftMargin;
                                yDelta = y - layoutParams.topMargin;
                                break;

                            case MotionEvent.ACTION_MOVE:
                                layoutParams = (ConstraintLayout.LayoutParams) PatchView.this.getLayoutParams();
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                PatchView.this.setLayoutParams(layoutParams);
                                wireDrawer.invalidate();
                                break;
                            case MotionEvent.ACTION_UP:
                                View delete = patchGraphFragment.getActivity().findViewById(R.id.menuDelete);
                                Rect bounds = new Rect();
                                int[] location = new int[2];
                                delete.getLocationOnScreen(location);
                                delete.getHitRect(bounds);
                                if (bounds.contains(x - location[0] + bounds.left, y - location[1] + bounds.top)) {
                                    patchPresenter.delete(patchId);
                                    wireDrawer.invalidate();
                                    ((ViewManager)PatchView.this.getParent()).removeView(PatchView.this);
                                }
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
        View.OnTouchListener listener =
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {

                            case MotionEvent.ACTION_DOWN:
                                patchPresenter.setDragOn(PatchView.this.patchId, (int) view.getTag());
                                wireDrawer.startDraw(view, PatchView.this.getColor());
                                break;
                            case MotionEvent.ACTION_MOVE:
                                wireDrawer.draw(x, y);
                                break;
                            case MotionEvent.ACTION_UP:
                                patchPresenter.setDragUp(x, y);
                                wireDrawer.release();
                            default:
                                break;
                        }
                        return true;
                    }
                };

        for (View view : outputs) {
            view.setOnTouchListener(listener);
        }
    }
}