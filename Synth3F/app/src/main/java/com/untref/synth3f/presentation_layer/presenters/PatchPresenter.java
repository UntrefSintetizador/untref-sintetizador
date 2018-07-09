package com.untref.synth3f.presentation_layer.presenters;

import android.view.View;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

import java.lang.reflect.Field;

public abstract class PatchPresenter {

    protected PatchView patchView;
    protected PatchGraphPresenter patchGraphPresenter;
    protected BaseProcessor processor;

    protected int numberOfInputs;
    protected int numberOfOutputs;
    protected String name;

    protected Patch patch;

    public PatchPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter, Patch patch) {
        this.patchView = patchView;
        this.patch = patch;
        this.patchGraphPresenter = patchGraphPresenter;
        this.processor = patchGraphPresenter.getProcessor();
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }

    public void setDragOn(int patchId, View output) {
        patchGraphPresenter.setDragOn(patchId, output);
    }

    public void setDragUp(int x, int y) {
        patchGraphPresenter.tryConnect(x, y);
    }

    public abstract PatchMenuView createMenuView(MainActivity context);

    public void setValue(String name, float value) {
        processor.sendValue(name, value);
    }

    public void delete(int patchId) {
        patchGraphPresenter.delete(patchId);
        processor.delete(patch, name + Integer.toString(patchId));
    }
}