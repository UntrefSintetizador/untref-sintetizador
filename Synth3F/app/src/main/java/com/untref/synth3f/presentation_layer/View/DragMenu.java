package com.untref.synth3f.presentation_layer.View;

public class DragMenu {

    private final int[] viewVisibilities;
    private final int visible;
    private final int gone;
    private final int pageSize;
    private int currentPageIndex = 0;

    private int[][] pages = new int[][] {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11}
    };

    public DragMenu(int[] viewVisibilities, int pageSize, int visible, int gone) {
        this.viewVisibilities = viewVisibilities.clone();
        this.pageSize = pageSize;
        this.visible = visible;
        this.gone = gone;
        this.viewVisibilities[0] = visible;
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
            viewIndex = viewVisibilities.length + viewIndex;
        }
        return viewVisibilities[viewIndex];
    }

    public void open() {
        viewVisibilities[1] = visible;
        viewVisibilities[viewVisibilities.length - 1] = visible;
        for (int i = 2; i < 2 + pageSize; i++) {
            viewVisibilities[i] = visible;
        }
    }

    public void close() {
        viewVisibilities[1] = gone;
        viewVisibilities[viewVisibilities.length - 1] = gone;
        for (int i = 2; i < 2 + pageSize; i++) {
            viewVisibilities[i] = gone;
        }
    }
}
