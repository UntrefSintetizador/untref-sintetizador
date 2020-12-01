package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;

import com.untref.synth3f.R;

public class EnvelopeEditor extends AppCompatImageView {

    private static final int NUM_OF_COLUMNS = 11;
    private static final int NUM_OF_ROWS = 8;

    private int width;
    private int height;
    private Paint borderPaint;
    private RectF borderRect;
    private Paint pointPaint;
    private float[] horizontalLinePts;
    private float[] verticalLinePts;
    private float cellHeight;
    private float cellWidth;
    private Paint linePaint;
    private boolean open;
    private float attack;
    private float decay;
    private float sustain;
    private float release;

    public EnvelopeEditor(Context context) {
        super(context);
    }

    public EnvelopeEditor(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        setBackgroundColor(0);
    }

    public void open(int color, float attack, float decay, float sustain, float release) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
        open = true;
        initPainting(color);
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
        canvas.drawLines(horizontalLinePts, linePaint);
        canvas.drawLines(verticalLinePts, linePaint);
        canvas.drawCircle(borderRect.left + cellWidth, borderRect.bottom - cellHeight * 2,
                          20, pointPaint);
    }

    private void initPainting(int color) {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(color);
        borderPaint.setStrokeWidth(4);
        borderRect = new RectF(30, 30, width - 30, height);
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(color);
        initLinePoints();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.white));
        linePaint.setAlpha(0x64);
    }

    private void initLinePoints() {
        cellHeight = borderRect.height() / NUM_OF_ROWS;
        horizontalLinePts = new float[NUM_OF_ROWS * 4];
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            int j = i * 4;
            horizontalLinePts[j] = borderRect.left;
            horizontalLinePts[j + 1] = borderRect.top + i * cellHeight;
            horizontalLinePts[j + 2] = horizontalLinePts[j] + borderRect.width();
            horizontalLinePts[j + 3] = horizontalLinePts[j + 1];
        }
        cellWidth = borderRect.width() / NUM_OF_COLUMNS;
        verticalLinePts = new float[NUM_OF_COLUMNS * 4];
        for (int i = 0; i < NUM_OF_COLUMNS; i++) {
            int j = i * 4;
            verticalLinePts[j] = borderRect.left + i * cellWidth;
            verticalLinePts[j + 1] = borderRect.top;
            verticalLinePts[j + 2] = verticalLinePts[j];
            verticalLinePts[j + 3] = verticalLinePts[j + 1] + borderRect.height();
        }
    }

}
