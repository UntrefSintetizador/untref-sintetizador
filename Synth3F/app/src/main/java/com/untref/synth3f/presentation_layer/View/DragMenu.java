package com.untref.synth3f.presentation_layer.View;

public class DragMenu {

    private static final int FIRST_PAGE_FIRST_INDEX = 2;
    private final int[] viewVisibilities;
    private final int visible;
    private final int gone;
    private final int pageSize;
    private int pageFirstIndex = FIRST_PAGE_FIRST_INDEX;
    private int numberOfButtonsInPages;

    public DragMenu(int[] viewVisibilities, int pageSize, int visible, int gone) {
        this.viewVisibilities = viewVisibilities.clone();
        this.pageSize = pageSize;
        this.visible = visible;
        this.gone = gone;
        this.viewVisibilities[0] = visible;
        numberOfButtonsInPages = viewVisibilities.length - 1;
    }

    public void scrollLeft() {
        int previousPageFirstIndex = pageFirstIndex - pageSize;
        if (previousPageFirstIndex < FIRST_PAGE_FIRST_INDEX) {
            previousPageFirstIndex = numberOfButtonsInPages - pageSize;
        }
        for (int i = pageFirstIndex; i < pageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = gone;
        }
        for (int i = previousPageFirstIndex; i < previousPageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = visible;
        }
        pageFirstIndex = previousPageFirstIndex;
    }

    public void scrollRight() {
        int nextPageFirstIndex = pageFirstIndex + pageSize;
        if (nextPageFirstIndex >= numberOfButtonsInPages) {
            nextPageFirstIndex = FIRST_PAGE_FIRST_INDEX;
        }
        int nextPageLastIndex = Math.min(nextPageFirstIndex + pageSize, numberOfButtonsInPages);
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
        for (int i = pageFirstIndex; i < pageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = visible;
        }
    }

    public void close() {
        viewVisibilities[1] = gone;
        viewVisibilities[viewVisibilities.length - 1] = gone;
        for (int i = FIRST_PAGE_FIRST_INDEX; i < numberOfButtonsInPages; i++) {
            viewVisibilities[i] = gone;
        }
    }
}
