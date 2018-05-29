package com.untref.synth3f.presentation_layer.presenters;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;
import com.untref.synth3f.presentation_layer.View.PatchMenuView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

public abstract class PatchPresenter {

    protected PatchView patchView;
    protected PatchGraphPresenter patchGraphPresenter;
    protected BaseProcessor processor;

    protected int numberOfInputs;
    protected int numberOfOutputs;
    protected String name;

    public PatchPresenter(PatchView patchView, PatchGraphPresenter patchGraphPresenter) {
        this.patchView = patchView;
        this.patchGraphPresenter = patchGraphPresenter;
        this.processor = patchGraphPresenter.getProcessor();
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }

    public void setDragOn(int patchId, int outputId) {
        patchGraphPresenter.setDragOn(patchId, outputId);
    }

    public void setDragUp(int x, int y) {
        patchGraphPresenter.tryConnect(x, y);
    }

    public abstract PatchMenuView createMenuView(MainActivity context);

    public void setValue(String name, float value) {
        processor.sendValue(name, value);
    }

    public void delete(int patchId){
        patchGraphPresenter.delete(patchId);
        processor.delete(patchId,name + Integer.toString(patchId));
    }
}