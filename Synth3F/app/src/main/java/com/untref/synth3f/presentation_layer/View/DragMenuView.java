package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DragMenuView extends LinearLayout {

    private static final int PAGE_SIZE = 5;
    private List<List<Button>> patchButtons;

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
        int pageIndex = 0;
        int count = getChildCount();
        boolean makeVisible = true;
        List<Button> firstPage = new ArrayList<>();
        patchButtons = new ArrayList<>();
        patchButtons.add(firstPage);

        for (int i = 2; i < count - 1; i++) {
            Button child = (Button) getChildAt(i);
            List<Button> currentPage = patchButtons.get(pageIndex);
            child.setVisibility(GONE);

            if (makeVisible) {
                child.setVisibility(VISIBLE);
            }

            currentPage.add(child);

            if (currentPage.size() > PAGE_SIZE) {
                List<Button> nextPage = new ArrayList<>();
                patchButtons.add(nextPage);
                pageIndex++;
            }
        }
    }
}
