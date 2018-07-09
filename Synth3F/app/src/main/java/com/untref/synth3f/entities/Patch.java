package com.untref.synth3f.entities;

import com.untref.synth3f.domain_layer.helpers.BaseProcessor;

import java.util.ArrayList;
import java.util.List;

public abstract class Patch {

    private int id;
    private List<Connection> outputConnections;
    private List<Connection> inputConnections;

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

    public abstract void initialize(BaseProcessor processor);
}
