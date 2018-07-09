package com.untref.synth3f.entities;

public class Connection {

    private int id;
    private int sourcePatch;
    private int sourceOutlet;
    private int targetPatch;
    private int targetInlet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourcePatch() {
        return sourcePatch;
    }

    public void setSourcePatch(int originPatch) {
        this.sourcePatch = originPatch;
    }

    public int getSourceOutlet() {
        return sourceOutlet;
    }

    public void setSourceOutlet(int originOutlet) {
        this.sourceOutlet = originOutlet;
    }

    public int getTargetPatch() {
        return targetPatch;
    }

    public void setTargetPatch(int endPatch) {
        this.targetPatch = endPatch;
    }

    public int getTargetInlet() {
        return targetInlet;
    }

    public void setTargetInlet(int endOutlet) {
        this.targetInlet = endOutlet;
    }

}
