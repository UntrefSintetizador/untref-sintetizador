package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;

import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

public class OptionsMenuView extends TableLayout {

    private PatchGraphFragment patchGraphFragment;
    private boolean opened;

    public OptionsMenuView(Context context) {
        super(context);
    }

    public OptionsMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public void setPatchGraphFragment(PatchGraphFragment patchGraphFragment) {
        this.patchGraphFragment = patchGraphFragment;

        setBackgroundColor(Color.BLUE);
        close();
    }

    public boolean toogle() {

        if (opened) {
            close();

        } else {
            open();
        }

        return opened;
    }

    private void open() {
        opened = true;
        setVisibility(View.VISIBLE);
    }

    private void close() {
        opened = false;
        setVisibility(View.GONE);
    }
}
