package com.example.ddavi.prueba.Processors;

/**
 * Created by oargueyo on 31/10/16.
 */

import com.example.ddavi.prueba.Processors.*;

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