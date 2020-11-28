package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.untref.synth3f.R;

import java.math.BigDecimal;

public class Knob extends AppCompatImageView implements View.OnTouchListener {

    private static final int MAX_ROTATION = 135;

    private PatchMenuView patchMenuView;

    private String parameterName;
    private float maxValue;
    private float minValue;
    private int precision;
    private float value;
    private MenuScaleFunction scaleFunction;

    private int rotationWhileNotMoving;
    private int initialTouch;
    private Knob linkedKnob;
    private String linkedKnobParameterName;
    private LinkingFunction linkingFunction;
    private Paint centerCirclePaint;
    private Paint borderCirclePaint;
    private Paint markPaint;

    public Knob(Context context) {
        super(context);
    }

    /**
     *  @param context contexto
     * @param patchMenuView patchMenuView
     * @param parameterName el nombre (string) que representa al knob
     * @param minValue valor minimo que puede representar el knob
     * @param maxValue valor maximo que puede representar el knob
     * @param precision la precision de los ajustes de los valores
     * @param value valor actual que representa el knob
     * @param scaleFunction la funcion de escala de los valores
     * @param color color
     * @param knobSize
     */
    public Knob(Context context, PatchMenuView patchMenuView, String parameterName,
                float minValue, float maxValue, int precision, float value,
                MenuScaleFunction scaleFunction, int color) {
        super(context);
        this.patchMenuView = patchMenuView;
        this.parameterName = parameterName;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.precision = precision;
        this.value = value;
        this.scaleFunction = scaleFunction;
        init(color);
    }

    /**
     *
     * @return devuelve el parameterName, el nombre (string) que lo representa.
     */
    public String getName() {
        return parameterName;
    }

    /**
     *
     * @return el valor que esta representando segun los movimientos que se la hayan aplicado al
     * knob.
     */
    public float getValue() {
        return value;
    }

    /**
     * Setea el valor del knob, y a su vez si tiene kobs vinculados, ejecuta las funciones necesarias
     * para setear de manera correcta tambien al knob vinculado.
     *
     * @param value el valor que va a representar el knob.
     * @return el valor normalizado.
     */
    public float setValue(float value) {
        setValueCheckingLinks(value, true);
        return this.value;
    }

    /**
     *
     * @param knob knob con el cual se va a vincular
     * @param parameterName nombre del parameterName (nombre que representa al knob) del knob al
     *                      cual se va a vincular
     * @param linkingFunction la funcion que indica la relacion que va a tener el knob que ejecuta
     *                        esta funcion con el knob con el cual se vincula
     */
    public void link(Knob knob, String parameterName, LinkingFunction linkingFunction) {
        linkedKnob = knob;
        linkedKnobParameterName = parameterName;
        this.linkingFunction = linkingFunction;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * Al tocar el knob, segun los movimientos que se le hagan, se ajustan los parametros que este
     * representa.
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int y = (int) motionEvent.getRawY();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                initialTouch = y;
                patchMenuView.setParameterToEdit(parameterName, value);
                break;

            case MotionEvent.ACTION_MOVE:
                int clampedRotation = getClampedRotation(rotationWhileNotMoving, y, initialTouch);
                convertRotationToValue(clampedRotation);
                setRotation(calculateRotation());
                checkLinkedKnob();
                patchMenuView.setValue(parameterName, value, true);
                break;

            case MotionEvent.ACTION_UP:
                rotationWhileNotMoving = getClampedRotation(rotationWhileNotMoving, y, initialTouch);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getWidth() / 2;
        int centerCircleSize = getHeight() / 4;
        int borderCircleSize = getHeight() * 5 / 16;
        canvas.drawCircle(centerX, centerY, centerCircleSize, centerCirclePaint);
        canvas.drawCircle(centerX, centerY, borderCircleSize, borderCirclePaint);
        int markLeft = centerX - 5;
        int markTop = borderCircleSize / 2;
        int markRight = centerX + 5;
        int markBottom = borderCircleSize * 5 / 4;
        canvas.drawRect(markLeft, markTop, markRight, markBottom, markPaint);
    }

    private void init(int color) {
        rotationWhileNotMoving = calculateRotation();
        setRotation(rotationWhileNotMoving);
        setClickable(true);
        setOnTouchListener(this);
        setBackgroundColor(0);
        centerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerCirclePaint.setColor(color);
        borderCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderCirclePaint.setStyle(Paint.Style.STROKE);
        borderCirclePaint.setColor(color);
        borderCirclePaint.setStrokeWidth(10);
        markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markPaint.setColor(getResources().getColor(R.color.white));
        setScaleType(AppCompatImageView.ScaleType.CENTER_INSIDE);
    }

    private void setValueCheckingLinks(float value, boolean checksLink) {
        this.value = Math.max(minValue, Math.min(maxValue, value));
        normalizeValue();
        rotationWhileNotMoving = calculateRotation();
        setRotation(rotationWhileNotMoving);

        if (checksLink) {
            checkLinkedKnob();
        }
    }

    private void normalizeValue() {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP);
        value = bigDecimal.floatValue();
    }

    private void checkLinkedKnob() {

        if (linkedKnob != null) {
            linkedKnob.setValueCheckingLinks(linkingFunction.calculate(value), false);
            patchMenuView.setValue(linkedKnobParameterName, linkedKnob.getValue(), false);
        }
    }

    private int getClampedRotation(int rotation, int y, int initialTouch) {
        return Math.max(-MAX_ROTATION, Math.min(MAX_ROTATION, rotation - y + initialTouch));
    }

    private int calculateRotation() {
        float percentage = scaleFunction.calculateInverse(value);

        return (int) (percentage * MAX_ROTATION * 2) - MAX_ROTATION;
    }

    private void convertRotationToValue(int rotation) {
        float percentage = (float) (rotation + MAX_ROTATION) / (MAX_ROTATION * 2);
        value = scaleFunction.calculate(percentage);
        normalizeValue();
    }
}
