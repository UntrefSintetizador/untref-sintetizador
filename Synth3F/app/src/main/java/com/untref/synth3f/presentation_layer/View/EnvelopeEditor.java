package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;

import java.math.BigDecimal;


public class EnvelopeEditor extends AppCompatImageView implements View.OnTouchListener {

    private static final int NUM_OF_COLUMNS = 11;
    private static final int NUM_OF_ROWS = 12;
    private static final int POINT_RADIUS = 20;
    private static final int ATTACK = 0;
    private static final int DECAY = 1;
    private static final int SUSTAIN = 2;
    private static final int RELEASE = 3;

    private Paint borderPaint;
    private RectF borderRect;
    private Paint pointPaint;
    private Paint selectionPaint;
    private Paint envelopeStrokePaint;
    private Paint envelopeFillPaint;
    private float[] horizontalLinePts;
    private float[] verticalLinePts;
    private float cellHeight;
    private float cellWidth;
    private Paint linePaint;
    private boolean open;
    private PointF startPoint;
    private EnvelopePoint[] envelopePoints;
    private Path envelopePath;
    private EnvelopePoint touchedPoint;
    private PatchMenuView patchMenuView;

    public EnvelopeEditor(Context context) {
        super(context);
    }

    public EnvelopeEditor(Context context, PatchMenuView patchMenuView, int width, int height) {
        super(context);
        this.patchMenuView = patchMenuView;
        initPainting();
        borderRect = new RectF(30, 30, width - 30, height);
        envelopePath = new Path();
        initLinePoints();
        setBackgroundColor(0);
        setOnTouchListener(this);
    }

    public void open(Parameter attack, Parameter decay, Parameter sustain, Parameter release,
                     int color) {
        initEnvelopePoints(attack, decay, sustain, release);
        updateEnvelopePath();
        open = true;
        borderPaint.setColor(color);
        pointPaint.setColor(color);
        selectionPaint.setColor(color);
        selectionPaint.setAlpha(0x7F);
        envelopeStrokePaint.setColor(color);
        envelopeFillPaint.setColor(color);
        envelopeFillPaint.setAlpha(0x7F);
    }

    public void close() {
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public Parameter getFirstParameter() {
        return envelopePoints[0].parameter;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                for (EnvelopePoint point : envelopePoints) {
                    RectF pointHitBox = new RectF(point.x - POINT_RADIUS * 2,
                                                  point.y - POINT_RADIUS * 2,
                                                  point.x + POINT_RADIUS * 2,
                                                  point.y + POINT_RADIUS * 2);
                    if (pointHitBox.contains(event.getX(), event.getY())) {
                        touchedPoint = point;
                        patchMenuView.setParameterToEdit(point.parameter.getName(),
                                                         point.parameter.getValue());
                        invalidate();
                        break;
                    }
                }
                if (touchedPoint == null) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchedPoint == null) {
                    return false;
                }
                /* TODO: Fix case of overlapping envelope points
                   set attack = 5000, decay = 5000, sustain = 0, and try to move sustain*/
                touchedPoint.move(event.getX(), event.getY());
                Parameter parameter = touchedPoint.parameter;
                patchMenuView.setValue(parameter.getName(), parameter.getValue(), true);
                updateEnvelopePath();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (touchedPoint == null) {
                    return false;
                }
                touchedPoint = null;
                break;
        }
        return true;
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
        canvas.drawCircle(startPoint.x, startPoint.y, POINT_RADIUS, pointPaint);
        for (EnvelopePoint point : envelopePoints) {
            if (touchedPoint == point) {
                canvas.drawCircle(point.x, point.y, POINT_RADIUS * 2, selectionPaint);
                canvas.drawCircle(point.x, point.y, (int) (POINT_RADIUS * 3 / 2), selectionPaint);
            }
            canvas.drawCircle(point.x, point.y, POINT_RADIUS, pointPaint);
        }
    }

    private void initPainting() {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4);
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectionPaint.setStyle(Paint.Style.STROKE);
        selectionPaint.setStrokeWidth(2);
        envelopeStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        envelopeStrokePaint.setStyle(Paint.Style.STROKE);
        envelopeStrokePaint.setStrokeWidth(10);
        envelopeFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        envelopeFillPaint.setStyle(Paint.Style.FILL);
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

    private void initEnvelopePoints(Parameter attack, Parameter decay, Parameter sustain,
                                    Parameter release) {
        startPoint = new PointF(borderRect.left + cellWidth,
                                borderRect.bottom - cellHeight * 2);
        envelopePoints = new EnvelopePoint[4];
        envelopePoints[ATTACK] = new EnvelopePoint(ATTACK, cellWidth * 3, attack);
        envelopePoints[DECAY] = new EnvelopePoint(DECAY, cellWidth * 3, decay);
        envelopePoints[SUSTAIN] = new EnvelopePoint(SUSTAIN, cellHeight * 7, sustain);
        envelopePoints[RELEASE] = new EnvelopePoint(RELEASE, cellWidth * 3, release);
        envelopePoints[SUSTAIN].updateNeighbors();
    }

    private void updateEnvelopePath() {
        envelopePath.rewind();
        envelopePath.moveTo(startPoint.x, startPoint.y);
        for (EnvelopePoint parameterPoint : envelopePoints) {
            envelopePath.lineTo(parameterPoint.x, parameterPoint.y);
        }
    }

    private class EnvelopePoint {
        private int id;
        private float x;
        private float y;
        private float range;
        private final float initRange;
        private Parameter parameter;

        public EnvelopePoint(int id, float range, Parameter parameter) {
            this.id = id;
            this.range = range;
            this.initRange = range;
            this.parameter = parameter;
            link();
        }

        public void move(float x, float y) {
            float value = parameter.getValue();
            float oldX = this.x;
            switch (id) {
                case ATTACK:
                    this.x = Math.max(startPoint.x, Math.min(startPoint.x + range, x));
                    envelopePoints[DECAY].x += this.x - oldX;
                    value = convertPositionToValue(this.x, startPoint.x, range);
                    break;
                case DECAY:
                    this.x = Math.max(envelopePoints[ATTACK].x,
                                      Math.min(envelopePoints[ATTACK].x + range, x));
                    if (range > 0) {
                        value = convertPositionToValue(this.x, envelopePoints[ATTACK].x, range);
                    }
                    break;
                case SUSTAIN:
                    this.y = Math.max(startPoint.y - range, Math.min(startPoint.y, y));
                    updateNeighbors();
                    value = convertPositionToValue(this.y, startPoint.y, -range);
                    break;
                case RELEASE:
                    this.x = Math.max(envelopePoints[SUSTAIN].x,
                                      Math.min(envelopePoints[SUSTAIN].x + range, x));
                    if (range > 0) {
                        value = convertPositionToValue(this.x, envelopePoints[SUSTAIN].x, range);
                    }
                    break;
            }
            parameter.setValue(value);
        }

        public void updateNeighbors() {
            envelopePoints[DECAY].range = envelopePoints[DECAY].initRange / -range *
                                          ((startPoint.y - range) - this.y);
            envelopePoints[DECAY].x = calculatePosition(envelopePoints[ATTACK].x,
                                                        envelopePoints[DECAY].range,
                                                        envelopePoints[DECAY].parameter);
            envelopePoints[DECAY].y = this.y;

            envelopePoints[RELEASE].range = envelopePoints[RELEASE].initRange / range *
                                            (startPoint.y - this.y);
            envelopePoints[RELEASE].x = calculatePosition(envelopePoints[SUSTAIN].x,
                                                          envelopePoints[RELEASE].range,
                                                          envelopePoints[RELEASE].parameter);
        }

        private void link() {
            switch (id) {
                case ATTACK:
                    x = calculatePosition(startPoint.x, range, parameter);
                    y = startPoint.y - cellHeight * 7;
                    break;
                case DECAY:
                    x = calculatePosition(envelopePoints[ATTACK].x, range, parameter);
                    break;
                case SUSTAIN:
                    x = startPoint.x + cellWidth * 6;
                    y = calculatePosition(startPoint.y, -range, parameter);
                    break;
                case RELEASE:
                    x = calculatePosition(envelopePoints[SUSTAIN].x, range, parameter);
                    y = startPoint.y;
                    break;
            }
        }

        private float calculatePosition(float minPosition, float range, Parameter parameter) {
            float percentage = parameter.getScaleFunction().calculateInverse(parameter.getValue());
            return percentage * range + minPosition;
        }

        private float convertPositionToValue(float position, float minPosition, float range) {
            float percentage = (position - minPosition) / range;
            float value = parameter.getScaleFunction().calculate(percentage);
            BigDecimal bigDecimal = BigDecimal.valueOf(value);
            bigDecimal = bigDecimal.setScale(parameter.getPrecision(), BigDecimal.ROUND_HALF_UP);
            return bigDecimal.floatValue();
        }
    }
}
