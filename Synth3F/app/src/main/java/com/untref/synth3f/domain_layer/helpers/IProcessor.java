package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;


public interface IProcessor {

    void sendValue(String name, Float value);
    void createPatch(String type, int patchId);
    void connect(Connection connection);
    void delete(Patch patch);
    void clear(Patch[] patches);
}