package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.untref.synth3f.domain_layer.helpers.ScrollMenu;

public class ScrollMenuView extends LinearLayout {

    private static final int PAGE_SIZE = 4;
    private boolean opened;
    private ScrollMenu scrollMenu;

    public ScrollMenuView(Context context) {
        super(context);
    }

    public ScrollMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        int[] viewVisibilities = new int[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            viewVisibilities[i] = GONE;
        }
        scrollMenu = new ScrollMenu(viewVisibilities, PAGE_SIZE, VISIBLE, GONE);
        updateVisibility();
    }

    public void scrollLeft() {
        scrollMenu.scrollLeft();
        updateVisibility();
    }

    public void scrollRight() {
        scrollMenu.scrollRight();
        updateVisibility();
    }

    public boolean toggle() {
        if (opened) {
            scrollMenu.close();
            opened = false;
        } else {
            scrollMenu.open();
            opened = true;
        }
        updateVisibility();

        return opened;
    }

    private void updateVisibility() {
        for (int i = 0; i < getChildCount(); i++) {
            int viewVisibility = scrollMenu.getVisibility(i);
            getChildAt(i).setVisibility(viewVisibility);
        }
    }
}
