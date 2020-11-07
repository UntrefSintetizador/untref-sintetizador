package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class OptionsMenuView extends TableLayout {

    private boolean opened;

    public OptionsMenuView(Context context) {
        super(context);
    }

    public OptionsMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        setBackgroundColor(Color.BLUE);
        close();
    }

    public boolean toggle() {
        if (opened) {
            close();
        } else {
            open();
        }

        return opened;
    }

    private void open() {
        opened = true;
        setVisibility(VISIBLE);
    }

    private void close() {
        opened = false;
        setVisibility(GONE);
    }
}
