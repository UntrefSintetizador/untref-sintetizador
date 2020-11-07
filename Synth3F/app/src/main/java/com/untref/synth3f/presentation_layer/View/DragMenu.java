package com.untref.synth3f.presentation_layer.View;

public class DragMenu {

    private final int[] viewVisibilities;
    private final int visible;
    private final int gone;
    private final int pageSize;
    private int pageFirstIndex;

    public DragMenu(int[] viewVisibilities, int pageSize, int visible, int gone) {
        this.viewVisibilities = viewVisibilities.clone();
        this.pageSize = pageSize;
        this.visible = visible;
        this.gone = gone;
        this.viewVisibilities[0] = visible;
        pageFirstIndex = 2;
    }

    public void scrollLeft() {}

    public void scrollRight() {
        int nextPageFirstIndex = pageFirstIndex + pageSize;
        int nextPageLastIndex = Math.min(nextPageFirstIndex + pageSize, viewVisibilities.length);
        for (int i = pageFirstIndex; i < pageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = gone;
        }
        for (int i = nextPageFirstIndex; i < nextPageLastIndex; i++) {
            viewVisibilities[i] = visible;
        }
        pageFirstIndex = nextPageFirstIndex;
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
