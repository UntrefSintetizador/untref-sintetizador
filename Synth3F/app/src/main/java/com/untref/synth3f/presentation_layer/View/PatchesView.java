package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class PatchesView extends LinearLayout {

    private int currentPageIndex = 0;
    private int[][] pages = new int[][] {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9}
    };

    public PatchesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PatchesView(Context context) {
        super(context);
    }

    public void init() {
        scroll(0);
    }

    public void scrollLeft() {
        scroll(-1);
    }

    public void scrollRight() {
        scroll(1);
    }

    private void scroll(int delta) {
        currentPageIndex = (currentPageIndex + delta) % pages.length;
        if (currentPageIndex < 0) {
            currentPageIndex = pages.length - 1;
        }
        int[] currentChildrenIndex = pages[currentPageIndex];
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
        for (int i = 0; i < currentChildrenIndex.length; i++) {
            getChildAt(currentChildrenIndex[i]).setVisibility(VISIBLE);
        }
    }
}
