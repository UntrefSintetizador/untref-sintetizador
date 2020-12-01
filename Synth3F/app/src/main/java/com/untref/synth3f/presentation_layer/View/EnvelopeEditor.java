package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.widget.LinearLayout;

public class EnvelopeEditor extends AppCompatImageView {

    private Paint borderPaint;
    private RectF borderRect;
    private boolean open;

    public EnvelopeEditor(Context context) {
        super(context);
        setBackgroundColor(0);
    }

    public void open(int width, int height, int color) {
        open = true;
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(color);
        borderPaint.setStrokeWidth(4);
        borderRect = new RectF(30, 30, width - 30, height);
    }

    public void close() {
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cornerRadius = 20;
        canvas.drawRoundRect(borderRect, cornerRadius, cornerRadius, borderPaint);
    }

}
