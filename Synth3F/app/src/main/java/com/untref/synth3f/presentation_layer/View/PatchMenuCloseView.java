package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.untref.synth3f.R;

public class PatchMenuCloseView extends AppCompatButton {

    private Paint closePaint;

    public PatchMenuCloseView(Context context) {
        super(context);
        init();
    }

    public PatchMenuCloseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = 45;
        int height = 15;
        int left = getWidth() / 2;
        int top = getHeight() / 2;
        int right = left + width;
        int bottom = top + height;
        canvas.drawRect(left, top, right, bottom, closePaint);
    }

    private void init() {
        closePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        closePaint.setColor(getResources().getColor(R.color.dark_grey));
    }

}
