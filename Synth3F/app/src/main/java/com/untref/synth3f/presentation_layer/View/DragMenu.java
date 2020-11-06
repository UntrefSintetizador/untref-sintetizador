package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DragMenu {

    private final int[] viewVisibilites;
    private final int visible;
    private final int gone;
    private int currentPageIndex = 0;

    private int[][] pages = new int[][] {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11}
    };

    public DragMenu(int[] viewVisibilities, int visible, int gone) {
        this.viewVisibilites = viewVisibilities.clone();
        this.visible = visible;
        this.gone = gone;
        this.viewVisibilites[0] = visible;
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
//        for (int i = 0; i < getChildCount(); i++) {
//            getChildAt(i).setVisibility(GONE);
//        }
//        for (int i = 0; i < currentChildrenIndex.length; i++) {
//            getChildAt(currentChildrenIndex[i]).setVisibility(VISIBLE);
//        }
    }

    public int getVisibility(int viewIndex) {
        if (viewIndex < 0) {
            viewIndex = viewVisibilites.length + viewIndex;
        }
        return viewVisibilites[viewIndex];
    }

    public void open() {
        viewVisibilites[1] = visible;
        viewVisibilites[viewVisibilites.length - 1] = visible;
    }
}
