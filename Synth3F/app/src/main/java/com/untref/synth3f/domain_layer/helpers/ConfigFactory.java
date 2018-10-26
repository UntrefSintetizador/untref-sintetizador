package com.untref.synth3f.domain_layer.helpers;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

//CAMBIAR NOMBRE
public class ConfigFactory {
    private static Map.Entry<String, String> currentEntry;
    private static LinkedHashMap<String, String> engines;
    private static Iterator<Map.Entry<String, String>> enginesIterator;

    static {
        engines = new LinkedHashMap<>();
        engines.put("PureData", "com.untref.synth3f.domain_layer.helpers.PureDataConfig");
        engines.put("Faust", "com.untref.synth3f.domain_layer.helpers.FaustConfig");
        enginesIterator = generateIterator();
        currentEntry = enginesIterator.next();

        Log.i("Engine", "Current engine is " + currentEntry.getKey());
    }

    public static void changeEngine() {

        if (!enginesIterator.hasNext()) {
            enginesIterator = generateIterator();
        }

        currentEntry = enginesIterator.next();
        Log.i("Engine", "Current engine is " + currentEntry.getKey());
    }

    public static IConfig create() {
        IConfig result = null;

        try {
            Class<?> configClass = Class.forName(currentEntry.getValue());
            Constructor constructor = configClass.getConstructor();
            result = (IConfig) constructor.newInstance();

        } catch (Exception a) {
            a.printStackTrace();
        }

        return result;
    }

    private static Iterator<Map.Entry<String, String>> generateIterator() {
        return engines.entrySet().iterator();
    }
}