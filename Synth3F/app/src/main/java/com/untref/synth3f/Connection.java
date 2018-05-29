package com.untref.synth3f;

public class Connection {

    private int originPatch;
    private int originOutlet;
    private int endPatch;
    private int endOutlet;

    public Connection(int originPatch, int originOutlet, int endPatch, int endOutlet){
        this.originPatch = originPatch;
        this.originOutlet = originOutlet;
        this.endPatch = endPatch;
        this.endOutlet = endOutlet;
    }

    public int getOriginPatch() {
        return originPatch;
    }

    public void setOriginPatch(int originPatch) {
        this.originPatch = originPatch;
    }

    public int getOriginOutlet() {
        return originOutlet;
    }

    public void setOriginOutlet(int originOutlet) {
        this.originOutlet = originOutlet;
    }

    public int getEndPatch() {
        return endPatch;
    }

    public void setEndPatch(int endPatch) {
        this.endPatch = endPatch;
    }

    public int getEndOutlet() {
        return endOutlet;
    }

    public void setEndOutlet(int endOutlet) {
        this.endOutlet = endOutlet;
    }

}
