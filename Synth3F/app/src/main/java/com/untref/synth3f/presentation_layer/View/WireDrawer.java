package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.untref.synth3f.Connection;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;

import java.util.List;

public class WireDrawer extends View {

    private Paint mPaint;
    private PatchGraphPresenter patchGraphPresenter;

    private boolean busy;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public WireDrawer(Context context, PatchGraphPresenter patchGraphPresenter) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10.0f);
        this.patchGraphPresenter = patchGraphPresenter;
    }

    public WireDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WireDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void release() {
        this.busy = false;
        this.invalidate();
    }

    public void startDraw(View actual, int color) {
        int[] position = getPositionOfView(actual);
        busy = true;
        startX = position[0];
        startY = position[1];
        mPaint.setColor(color);
    }

    public void draw(int x, int y) {
        endX = x;
        endY = y;
        invalidate();
    }

    private int[] getPositionOfView(View view) {
        int[] position = new int[2];
        Rect rect = new Rect();
        view.getLocationOnScreen(position);
        view.getHitRect(rect);
        position[0] += (rect.right - rect.left) / 2;
        position[1] += (rect.bottom - rect.top) / 2;
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (busy) {
            this.drawLine(startX, startY, endX, endY, canvas);
        }
        int[] start;
        int[] end;
        SparseArray<PatchView> patches = patchGraphPresenter.getPatches();
        SparseArray<List<Connection>> connectionsSet = patchGraphPresenter.getConnections();
        for (int i = 0; i < connectionsSet.size(); i++) {
            PatchView patch = patches.get(connectionsSet.keyAt(i));
            List<Connection> connections = connectionsSet.valueAt(i);
            mPaint.setColor(patch.getColor());
            for (Connection connection : connections) {
                start = getPositionOfView(patch.getOutputs()[connection.getOriginOutlet()]);
                end = getPositionOfView(patches.get(connection.getEndPatch()).getInputs()[connection.getEndOutlet()]);
                this.drawLine(start[0], start[1], end[0], end[1], canvas);
            }
        }
    }

    private void drawLine(int startX, int startY, int endX, int endY, Canvas canvas) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((startX + endX) / 2, (startY + endY) / 2 + Math.abs(endX - startX) / 4, endX, endY);
        canvas.drawPath(path, mPaint);
    }
}
