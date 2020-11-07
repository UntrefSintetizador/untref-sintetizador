package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.untref.synth3f.R;

public class DragMenuView extends LinearLayout {

    private static final int PAGE_SIZE = 4;
    private int firstButtonIndex = 2;
    private boolean opened;
    private DragMenu dragMenu;

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
        int[] viewVisibilities = new int[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            viewVisibilities[i] = GONE;
        }
        dragMenu = new DragMenu(viewVisibilities, PAGE_SIZE, VISIBLE, GONE);
        updateVisibility();
    }

    public void scrollLeft() {
        dragMenu.scrollLeft();
        /*firstButtonIndex -= PAGE_SIZE;

        if (firstButtonIndex < 2) {
            firstButtonIndex = getChildCount() - 2;
        }

        updateVisibility();*/
    }

    public void scrollRight() {
        dragMenu.scrollRight();
        /*firstButtonIndex += PAGE_SIZE;

        if (firstButtonIndex > getChildCount() - 2) {
            firstButtonIndex = 2;
        }

        updateVisibility();*/
    }

    public void toogle() {
        if (opened) {
            close();
        } else {
            open();
        }
        updateVisibility();
    }

    private void updateVisibility() {
        for (int i = 0; i < getChildCount(); i++) {
            int viewVisibility = dragMenu.getVisibility(i);
            getChildAt(i).setVisibility(viewVisibility);
        }
    }

    private void close() {
        dragMenu.close();
        opened = false;
    }

    private void open() {
        dragMenu.open();
        opened = true;
    }

    private void setButtonsVisibility(int visibility) {
        getChildAt(1).setVisibility(visibility);
        getChildAt(getChildCount() - 1).setVisibility(visibility);
        int count = getChildCount() - 1;
        int v = visibility;

        for (int i = 1; i < count; i++) {
            View current = getChildAt(i);

            if (i == firstButtonIndex + PAGE_SIZE) {
                v = GONE;
            }

            current.setVisibility(v);
        }
    }
}
