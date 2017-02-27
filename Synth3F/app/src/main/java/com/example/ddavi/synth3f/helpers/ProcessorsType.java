package com.example.ddavi.synth3f.helpers;

/**
 * Created by oargueyo on 31/10/16.
 */


public enum ProcessorsType {

    FAUST("FaustProcessor"),
    PUREDATA("PureDataProcessor");

    private String p;

    private ProcessorsType(String p) {
        this.p = p;
    }

    public String getProccesor(){
        return this.p;
    }

}