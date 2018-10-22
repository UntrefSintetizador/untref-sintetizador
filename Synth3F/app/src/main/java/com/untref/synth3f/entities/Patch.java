package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.IProcessor;

import java.util.ArrayList;
import java.util.List;

public abstract class Patch {

    private int id;
    private List<Connection> outputConnections;
    private List<Connection> inputConnections;
    private float posX;
    private float posY;

    public Patch() {
        outputConnections = new ArrayList<>(0);
        inputConnections = new ArrayList<>(0);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Connection> getInputConnections() {
        return inputConnections;
    }

    public List<Connection> getOutputConnections() {
        return outputConnections;
    }

    public boolean addInputConnection(Connection connection) {
        return inputConnections.add(connection);
    }

    public boolean removeInputConnection(Connection connection) {
        return inputConnections.remove(connection);
    }

    public boolean addOutputConnection(Connection connection) {
        return outputConnections.add(connection);
    }

    public boolean removeOutputConnection(Connection connection) {
        return outputConnections.remove(connection);
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosX() {
        return this.posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosY() {
        return this.posY;
    }

    public abstract void initialize(IProcessor processor);

    public abstract String getTypeName();

    public abstract int getNumberOfInputs();

    public abstract int getNumberOfOutputs();
}
