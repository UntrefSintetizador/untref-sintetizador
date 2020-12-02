package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.AppCompatImageView;

import com.untref.synth3f.R;


public class EnvelopeEditor extends AppCompatImageView {

    private static final int NUM_OF_COLUMNS = 11;
    private static final int NUM_OF_ROWS = 12;

    private Paint borderPaint;
    private RectF borderRect;
    private Paint pointPaint;
    private Paint envelopeStrokePaint;
    private Paint envelopeFillPaint;
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
    private EnvelopePoint[] points;
    private Path envelopePath;

    public EnvelopeEditor(Context context) {
        super(context);
    }

    public EnvelopeEditor(Context context, int width, int height) {
        super(context);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4);
        borderRect = new RectF(30, 30, width - 30, height);
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        envelopeStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        envelopeStrokePaint.setStyle(Paint.Style.STROKE);
        envelopeStrokePaint.setStrokeWidth(10);
        envelopeFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        envelopeFillPaint.setStyle(Paint.Style.FILL);
        initLinePoints();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.white));
        linePaint.setAlpha(0x64);
        setBackgroundColor(0);
        initEnvelopePoints();
    }

    public void open(int color, float attack, float decay, float sustain, float release) {
        this.attack = attack;
        this.decay = decay;
        this.sustain = sustain;
        this.release = release;
        open = true;
        borderPaint.setColor(color);
        pointPaint.setColor(color);
        envelopeStrokePaint.setColor(color);
        envelopeFillPaint.setColor(ColorUtils.setAlphaComponent(color, 0x7F));
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
        canvas.drawPath(envelopePath, envelopeFillPaint);
        canvas.drawPath(envelopePath, envelopeStrokePaint);
        for (EnvelopePoint point : points) {
            canvas.drawCircle(point.x, point.y, 20, pointPaint);
        }
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

    private void initEnvelopePoints() {
        points = new EnvelopePoint[] {
                new EnvelopePoint(borderRect.left + cellWidth,
                                  borderRect.bottom - cellHeight * 2),
                new EnvelopePoint(borderRect.left + cellWidth * 3,
                                  borderRect.top + cellHeight * 3),
                new EnvelopePoint(borderRect.left + cellWidth * 4,
                                  borderRect.top + cellHeight * 6),
                new EnvelopePoint(borderRect.right - cellWidth * 4,
                                  borderRect.top + cellHeight * 6),
                new EnvelopePoint(borderRect.right - cellWidth,
                                  borderRect.bottom - cellHeight * 2)
        };

        envelopePath = new Path();
        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                envelopePath.moveTo(points[i].x, points[i].y);
            } else {
                envelopePath.lineTo(points[i].x, points[i].y);
            }
        }
    }

    private static class EnvelopePoint {
        private final float x;
        private final float y;

        public EnvelopePoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
