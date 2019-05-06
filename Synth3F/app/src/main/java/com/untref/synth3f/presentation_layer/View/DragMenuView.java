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
    private PatchesView patchesView;

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
        patchesView = findViewById(R.id.patches_view);
        patchesView.init();
        close();
    }

    public void scrollLeft() {
        patchesView.scrollLeft();
        /*firstButtonIndex -= PAGE_SIZE;

        if (firstButtonIndex < 2) {
            firstButtonIndex = getChildCount() - 2;
        }

        updateVisibility();*/
    }

    public void scrollRight() {
        patchesView.scrollRight();
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
    }

    private void updateVisibility() {
        int count = getChildCount() - 1;
        int index = firstButtonIndex;

        for (int i = 2; i < count; i++) {
            getChildAt(i).setVisibility(GONE);
        }

        for (int i = 0; i < PAGE_SIZE; i++) {
            getChildAt(index).setVisibility(VISIBLE);
            index++;

            if (index > getChildCount() - 2) {
                index = 2;
            }
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
