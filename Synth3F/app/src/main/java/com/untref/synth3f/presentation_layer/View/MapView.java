package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class MapView extends ConstraintLayout {

    public static float MIN_ZOOM = 1.0f;
    public static float MAX_ZOOM = 6.0f;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scale = MIN_ZOOM;
    private float translationX = 0.0f;
    private float translationY = 0.0f;
    private float startX;
    private float startY;
    private float startTranslationX = 0.0f;
    private float startTranslationY = 0.0f;

    public MapView(Context context) {
        this(context, null, 0);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public float getScale() {
        return scale;
    }

    public float convertLayoutToScreenX(float x) {
        return (x - getAffectedView().getWidth() / 2) * scale + translationX + getAffectedView().getWidth() / 2;
    }

    public float convertLayoutToScreenY(float y) {
        return (y - getAffectedView().getHeight() / 2) * scale + translationY + getAffectedView().getHeight() / 2;
    }

    public float convertScreenToLayoutX(float x) {
        return (x - translationX - getAffectedView().getWidth() / 2) / scale + getAffectedView().getWidth() / 2;
    }

    public float convertScreenToLayoutY(float y) {
        return (y - translationY - getAffectedView().getHeight() / 2) / scale + getAffectedView().getHeight() / 2;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                startTranslationX = translationX;
                startTranslationY = translationY;
                break;
        }
        boolean retVal = scaleGestureDetector.onTouchEvent(event);
        retVal = gestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }

    private void init(Context context) {
        ScaleGestureDetector.OnScaleGestureListener scaleGestureListener
                = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            private float initialFocusX;
            private float initialDistanceX;
            private float initialFocusY;
            private float initialDistanceY;

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                initialFocusX = scaleGestureDetector.getFocusX() - getAffectedView().getWidth() / 2;
                initialDistanceX = (initialFocusX - translationX) / scale;
                initialFocusY = scaleGestureDetector.getFocusY() - getAffectedView().getHeight() / 2;
                initialDistanceY = (initialFocusY - translationY) / scale;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                scale *= scaleGestureDetector.getScaleFactor();
                scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
                translationX = initialFocusX - initialDistanceX * scale;
                translationY = initialFocusY - initialDistanceY * scale;
                getAffectedView().setScaleX(scale);
                getAffectedView().setScaleY(scale);
                translate();
                return true;
            }
        };

        GestureDetector.SimpleOnGestureListener gestureListener
                = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e2.getPointerCount() == 1) {
                    translationX = startTranslationX - startX + e2.getRawX();
                    translationY = startTranslationY - startY + e2.getRawY();
                    translate();
                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        };
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
        gestureDetector = new GestureDetector(context, gestureListener);

        this.post(new Runnable() {
            @Override
            public void run() {
                scale = MAX_ZOOM;
                getAffectedView().setScaleX(scale);
                getAffectedView().setScaleY(scale);
            }
        });
    }

    private void translate() {
        float maxTranslationX = getAffectedView().getWidth() / 2 * scale - getAffectedView().getWidth() / 2;
        float maxTranslationY = getAffectedView().getHeight() / 2 * scale - getAffectedView().getHeight() / 2;
        translationX = Math.min(Math.max(translationX, -maxTranslationX), maxTranslationX);
        translationY = Math.min(Math.max(translationY, -maxTranslationY), maxTranslationY);
        getAffectedView().setX(translationX);
        getAffectedView().setY(translationY);
        ViewCompat.postInvalidateOnAnimation(getAffectedView());
    }

    private View getAffectedView() {
        return getChildAt(0);
    }
}