package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.presenters.PatchGraphPresenter;

import java.util.HashMap;
import java.util.Map;

public class WireDrawer extends View {

    private Paint mPaint;
    private PatchGraphPresenter patchGraphPresenter;
    private MapView mapView;
    private Map<Integer, Line> lines;
    Path path;

    private boolean drawing;
    private int color;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public WireDrawer(Context context, PatchGraphPresenter patchGraphPresenter, MapView mapView) {
        super(context);
        this.mapView = mapView;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(Color.RED);
        this.mPaint.setStrokeWidth(10.0f / MapView.MAX_ZOOM);
        this.patchGraphPresenter = patchGraphPresenter;
        this.lines = new HashMap<>();
        this.path = new Path();
    }

    public WireDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WireDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void release() {
        this.drawing = false;
        this.invalidate();
    }

    public void startDraw(View actual, int color) {
        int[] position = getPositionOfView(actual);
        this.drawing = true;
        this.startX = position[0];
        this.startY = position[1];
        this.endX = position[0];
        this.endY = position[1];
        this.color = color;
    }

    public void draw(int x, int y) {
        this.endX = (int) mapView.convertScreenToLayoutX(x);
        this.endY = (int) mapView.convertScreenToLayoutY(y);
        invalidate();
    }

    public void movePatch(int patchId, int x, int y) {
        this.endX = x;
        this.endY = y;
        Patch patch = patchGraphPresenter.getPatch(patchId);
        Line line;
        int[] location;
        for (Connection connection : patch.getOutputConnections()) {
            line = lines.get(connection.getId());
            location = getPositionOfView(line.startView);
            line.startX = location[0];
            line.startY = location[1];
            line.midX = (line.startX + line.endX) / 2;
            line.midY = (line.startY + line.endY) / 2 + Math.abs(line.endX - line.startX) / 4;
        }
        for (Connection connection : patch.getInputConnections()) {
            line = lines.get(connection.getId());
            location = getPositionOfView(line.endView);
            line.endX = location[0];
            line.endY = location[1];
            line.midX = (line.startX + line.endX) / 2;
            line.midY = (line.startY + line.endY) / 2 + Math.abs(line.endX - line.startX) / 4;
        }
        invalidate();
    }

    public void addConnection(Connection connection, View start, View end) {
        Line line = new Line();
        line.startView = start;
        line.endView = end;
        line.color = this.color;
        int[] startLocation = getPositionOfView(start);
        int[] endLocation = getPositionOfView(end);
        line.startX = startLocation[0];
        line.startY = startLocation[1];
        line.endX = endLocation[0];
        line.endY = endLocation[1];
        line.midX = (line.startX + line.endX) / 2;
        line.midY = (line.startY + line.endY) / 2 + Math.abs(line.endX - line.startX) / 4;
        lines.put(connection.getId(), line);
        invalidate();
    }

    public void removePatch(Patch patch) {
        for (Connection connection : patch.getOutputConnections()) {
            lines.remove(connection.getId());
        }
        for (Connection connection : patch.getInputConnections()) {
            lines.remove(connection.getId());
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawing) {
            path.reset();
            mPaint.setColor(color);
            path.moveTo(startX, startY);
            path.quadTo((startX + endX) / 2, (startY + endY) / 2 + Math.abs(endX - startX) / 4, endX, endY);
            canvas.drawPath(path, mPaint);
        }
        for (Line line : lines.values()) {
            path.reset();
            mPaint.setColor(line.color);
            path.moveTo(line.startX, line.startY);
            path.quadTo(line.midX, line.midY, line.endX, line.endY);
            canvas.drawPath(path, mPaint);
        }
    }

    private int[] getPositionOfView(View view) {
        int[] position = new int[2];
        Rect rect = new Rect();
        view.getLocationOnScreen(position);
        view.getHitRect(rect);
        position[0] += ((rect.right - rect.left) / 2) * mapView.getScale();
        position[1] += ((rect.bottom - rect.top) / 2) * mapView.getScale();
        position[0] = (int) mapView.convertScreenToLayoutX(position[0]);
        position[1] = (int) mapView.convertScreenToLayoutY(position[1]);
        return position;
    }

    private class Line {
        View startView;
        View endView;
        int color;
        int startX;
        int startY;
        int midX;
        int midY;
        int endX;
        int endY;
    }
}