package com.untref.synth3f.domain_layer.helpers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

//CAMBIAR NOMBRE
public class ConfigFactory {
    private static Map<String, String> engines;

    static {
        engines = new HashMap<>();
        engines.put("PureData", "com.untref.synth3f.domain_layer.helpers.PureDataConfig");
        engines.put("Faust", "com.untref.synth3f.domain_layer.helpers.FaustConfig");
    }

    public static IConfig create(String engineName) {
        IConfig result = null;

        try {
            Class<?> configClass = Class.forName(engines.get(engineName));
            Constructor constructor = configClass.getConstructor();
            result = (IConfig) constructor.newInstance();

        } catch (Exception a) {
            a.printStackTrace();
        }

        return result;
    }
}
