package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class PatchesLayout extends LinearLayout {

    private List<Button> patchButtons;

    public PatchesLayout(Context context) {
        super(context);
    }

    public PatchesLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PatchesLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        patchButtons = new ArrayList<>();
        patchButtons.add(new Button);
    }
}
