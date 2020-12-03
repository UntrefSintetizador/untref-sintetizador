package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Parameter;

import java.math.BigDecimal;


public class EnvelopeEditor extends AppCompatImageView implements View.OnTouchListener {

    private static final int NUM_OF_COLUMNS = 11;
    private static final int NUM_OF_ROWS = 12;
    private static final int POINT_RADIUS = 20;
    private static final int ATTACK_INDEX = 0;
    private static final int DECAY_INDEX = 1;
    private static final int SUSTAIN_INDEX = 2;
    private static final int RELEASE_INDEX = 3;

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
        initLinePoints();
        setBackgroundColor(0);
        setOnTouchListener(this);
    }

    public void open(Parameter attack, Parameter decay, Parameter sustain, Parameter release,
                     int color) {
        initEnvelopePoints(attack, decay, sustain, release);
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
                movePoint(touchedPoint, event.getX(), event.getY());
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
        envelopePoints[ATTACK_INDEX] = new EnvelopePoint(0, borderRect.top + cellHeight * 3,
                                                         cellWidth * 3, attack);
        envelopePoints[ATTACK_INDEX].x = calculatePosition(envelopePoints[ATTACK_INDEX],
                                                           startPoint.x);
        envelopePoints[DECAY_INDEX] = new EnvelopePoint(0, startPoint.y, cellWidth * 3,
                                                        decay);
        envelopePoints[DECAY_INDEX].x = calculatePosition(envelopePoints[DECAY_INDEX],
                                                          envelopePoints[ATTACK_INDEX].x);
        envelopePoints[SUSTAIN_INDEX] = new EnvelopePoint(borderRect.right - cellWidth * 4,
                                                          startPoint.y, cellWidth * 7,
                                                          sustain);
        envelopePoints[RELEASE_INDEX] = new EnvelopePoint(borderRect.right - cellWidth,
                                                           borderRect.bottom - cellHeight * 2,
                                                           cellWidth * 3, release);
        envelopePath = new Path();
        updateEnvelopePath();
    }

    private void updateEnvelopePath() {
        envelopePath.rewind();
        envelopePath.moveTo(startPoint.x, startPoint.y);
        for (EnvelopePoint parameterPoint : envelopePoints) {
            envelopePath.lineTo(parameterPoint.x, parameterPoint.y);
        }
    }

    private void movePoint(EnvelopePoint point, float x, float y) {
        float value = point.parameter.getValue();
        float oldX = point.x;
        if (point == envelopePoints[ATTACK_INDEX]) {
            point.x = Math.max(startPoint.x, Math.min(startPoint.x + point.range, x));
            envelopePoints[DECAY_INDEX].x += point.x - oldX;
            value = convertPositionToValue(point.x, startPoint.x, point.range, point.parameter);
        } else if (point == envelopePoints[DECAY_INDEX]) {
            point.x = Math.max(envelopePoints[ATTACK_INDEX].x,
                               Math.min(envelopePoints[ATTACK_INDEX].x + point.range, x));
            value = convertPositionToValue(point.x, envelopePoints[ATTACK_INDEX].x, point.range,
                                           point.parameter);
        }
        patchMenuView.setValue(point.parameter.getName(), value, true);
    }

    private float calculatePosition(EnvelopePoint point, float minValue) {
        Parameter parameter = point.parameter;
        float percentage = parameter.getScaleFunction().calculateInverse(parameter.getValue());
        return percentage * point.range + minValue;
    }

    private float convertPositionToValue(float position, float minPosition, float range,
                                         Parameter parameter) {
        float percentage = (position - minPosition) / range;
        float value = parameter.getScaleFunction().calculate(percentage);
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(parameter.getPrecision(), BigDecimal.ROUND_HALF_UP);
        return bigDecimal.floatValue();
    }

    private static class EnvelopePoint {
        private float x;
        private float y;
        private float range;
        private Parameter parameter;

        public EnvelopePoint(float x, float y, float range, Parameter parameter) {
            this.x = x;
            this.y = y;
            this.range = range;
            this.parameter = parameter;
        }
    }

}
