package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.untref.synth3f.domain_layer.helpers.DragMenu;

public class DragMenuView extends LinearLayout {

    private static final int PAGE_SIZE = 4;
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
        updateVisibility();
    }

    public void scrollRight() {
        dragMenu.scrollRight();
        updateVisibility();
    }

    public boolean toggle() {
        if (opened) {
            dragMenu.close();
            opened = false;
        } else {
            dragMenu.open();
            opened = true;
        }
        updateVisibility();

        return opened;
    }

    private void updateVisibility() {
        for (int i = 0; i < getChildCount(); i++) {
            int viewVisibility = dragMenu.getVisibility(i);
            getChildAt(i).setVisibility(viewVisibility);
        }
    }
}
