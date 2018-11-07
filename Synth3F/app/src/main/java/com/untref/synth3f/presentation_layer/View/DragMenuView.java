package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DragMenuView extends LinearLayout {

    private static final int PAGE_SIZE = 4;
    private List<List<Button>> patchButtonPages;
    private int pageIndex = 0;
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
        int count = getChildCount();
        boolean makeVisible = true;
        List<Button> firstPage = new ArrayList<>();
        patchButtonPages = new ArrayList<>();
        patchButtonPages.add(firstPage);

        for (int i = 2; i < count - 1; i++) {
            Button child = (Button) getChildAt(i);
            List<Button> currentPage = patchButtonPages.get(pageIndex);
            child.setVisibility(GONE);

            if (makeVisible) {
                child.setVisibility(VISIBLE);
            }

            currentPage.add(child);

            if (currentPage.size() == PAGE_SIZE) {
                List<Button> nextPage = new ArrayList<>();
                patchButtonPages.add(nextPage);
                makeVisible = false;
                pageIndex++;
            }
        }

        pageIndex = 0;
        close();
    }

    public void scrollLeft() {

        if (pageIndex > 0) {
            hideCurrentPage();
            pageIndex--;
            showCurrentPage();
        }
    }

    public void scrollRight() {

        if (pageIndex < patchButtonPages.get(pageIndex).size() - 1) {
            hideCurrentPage();
            pageIndex++;
            showCurrentPage();
        }
    }

    public void toogle() {

        if (opened) {
            close();

        } else {
            open();
        }
    }

    private void hideCurrentPage() {
        List<Button> currentPage = patchButtonPages.get(pageIndex);

        for (Button button : currentPage) {
            button.setVisibility(GONE);
        }
    }

    private void showCurrentPage() {
        List<Button> currentPage = patchButtonPages.get(pageIndex);

        for (Button button : currentPage) {
            button.setVisibility(VISIBLE);
        }
    }

    private void close() {
        setScrollButtonsVisibility(GONE);
        hideCurrentPage();
        opened = false;
    }

    private void open() {
        setScrollButtonsVisibility(VISIBLE);
        showCurrentPage();
        opened = true;
    }

    private void setScrollButtonsVisibility(int visibility) {
        getChildAt(1).setVisibility(visibility);
        getChildAt(getChildCount() - 1).setVisibility(visibility);
    }
}
