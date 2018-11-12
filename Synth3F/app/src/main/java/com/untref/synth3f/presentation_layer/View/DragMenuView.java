package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class DragMenuView extends LinearLayout {

    private static final int PAGE_SIZE = 4;
    private int firstButtonIndex = 1;
    private boolean opened;

    public DragMenuView(Context context) {
        super(context);
    }

    public DragMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        updateVisibility();
        close();
    }

    public void scrollLeft() {
        firstButtonIndex = (firstButtonIndex + PAGE_SIZE) % getChildCount();
        updateVisibility();
    }

    public void scrollRight() {
        firstButtonIndex = (firstButtonIndex - PAGE_SIZE) % getChildCount();
        updateVisibility();
    }

    public void toogle() {

        if (opened) {
            close();

        } else {
            open();
        }
    }

    private void updateVisibility() {
        int count = getChildCount();
        int visibility = GONE;

        for (int i = 0; i < count; i++) {
            View current = getChildAt(i);

            if (i == firstButtonIndex) {
                visibility = VISIBLE;

            } else if (i == PAGE_SIZE) {
                visibility = GONE;
            }

            current.setVisibility(visibility);
        }
    }

    private void close() {
        setButtonsVisibility(GONE);
        opened = false;
    }

    private void open() {
        setButtonsVisibility(VISIBLE);
        opened = true;
    }

    private void setButtonsVisibility(int visibility) {
        getChildAt(1).setVisibility(visibility);
        getChildAt(getChildCount() - 1).setVisibility(visibility);

        for (int i = 0; i < PAGE_SIZE; i++) {
            getChildAt(firstButtonIndex + i).setVisibility(visibility);
        }
    }
}
