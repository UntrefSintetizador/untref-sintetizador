package com.untref.synth3f.presentation_layer.presenters;

import android.view.View;

import com.untref.synth3f.domain_layer.helpers.IProcessor;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.MenuScaleFunction;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;

import java.lang.reflect.Field;

public abstract class PatchPresenter {

    protected static final int FLOAT_PRECISION = 5;
    protected static final int INTEGER_PRECISION = 0;

    protected PatchView patchView;
    protected PatchGraphPresenter patchGraphPresenter;
    protected IProcessor processor;
    protected Patch patch;

    public PatchPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        this.patchView = patchView;
        this.patch = patch;
        this.patchGraphPresenter = patchGraphPresenter;
        this.processor = patchGraphPresenter.getProcessor();
    }

    public void setDragOn(int patchId, View output) {
        patchGraphPresenter.setDragOn(patchId, output);
    }

    public void setDragUp(int x, int y, boolean isInlet) {
        patchGraphPresenter.tryConnect(x, y, isInlet);
    }

    public void disconnect(int patchId, View connector, boolean isInlet) {
        patchGraphPresenter.disconnect(patchId, connector, isInlet);
    }

    public abstract boolean initMenuView(PatchMenuView patchMenuView);

    public void setValue(String name, float value) {
        try {
            Class<?> c = patch.getClass();
            Field f = c.getDeclaredField(name.replace("-", "_"));
            f.setAccessible(true);
            f.setFloat(patch, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        processor.sendValue("x_" + patch.getTypeName() + "_" + Integer.toString(patch.getId()) + "_" + name, value);
    }

    public void delete(int patchId) {
        patchGraphPresenter.delete(patchId);
        processor.delete(patch);
    }

    protected class LinearFunction extends MenuScaleFunction {

        public LinearFunction(float minValue, float maxValue) {
            super(minValue, maxValue);
        }

        @Override
        public float calculate(float x) {
            return getMinValue() + (x * (getMaxValue() - getMinValue()));
        }

        @Override
        public float calculateInverse(float x) {
            return (x - getMinValue()) / (getMaxValue() - getMinValue());
        }
    }

    protected class ExponentialLeftFunction extends MenuScaleFunction {

        public ExponentialLeftFunction(float minValue, float maxValue) {
            super(minValue, maxValue);
        }

        @Override
        public float calculate(float x) {
            return (float) (getMinValue() + Math.pow(getMaxValue() + 1, x) - 1);
        }

        @Override
        public float calculateInverse(float x) {
            return (float) (Math.log(x - getMinValue() + 1) / Math.log(getMaxValue() + 1));
        }
    }

    protected class ExponentialCenterFunction extends MenuScaleFunction {

        public ExponentialCenterFunction(float minValue, float maxValue) {
            super(minValue, maxValue);
        }

        @Override
        public float calculate(float x) {
            float result;

            if (x > 0.5) {
                result = (float) Math.pow(getMaxValue() + 1, (x - 0.5) * 2) - 1;

            } else {
                result = (float) -Math.pow(-getMinValue() + 1, -(x - 0.5) * 2) + 1;
            }

            return result;
        }

        @Override
        public float calculateInverse(float x) {
            double aux;

            if (x > 0) {
                aux = Math.log(x + 1) / Math.log(getMaxValue() + 1);

            } else {
                aux = -Math.log(-x + 1) / Math.log(-getMinValue() + 1);
            }

            return (float) (aux + 1) / 2;
        }
    }
}