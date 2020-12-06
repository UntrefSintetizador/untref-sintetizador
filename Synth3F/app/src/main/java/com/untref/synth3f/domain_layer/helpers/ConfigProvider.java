package com.untref.synth3f.domain_layer.helpers;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Genera la configuracion de un motor de procesamiento de audio.
 */
public class ConfigProvider {
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

    /**
     * Cambia el motor de procesamiento de audio. Este motor se usara para generar la configuracion
     * correspondiente.
     */
    public static void changeEngine() {
        if (!enginesIterator.hasNext()) {
            enginesIterator = generateIterator();
        }

        currentEntry = enginesIterator.next();
        Log.i("Engine", "Current engine is " + currentEntry.getKey());
    }

    /**
     * Dado un motor de procesamiento de audio, retorna su configuracion correspondiente.
     *
     * @return la configuracion del motor de procesamiento de audio.
     */
    public static Config create() {
        Config result = null;

        try {
            Class<?> configClass = Class.forName(currentEntry.getValue());
            Constructor<?> constructor = configClass.getConstructor();
            result = (Config) constructor.newInstance();
        } catch (Exception a) {
            a.printStackTrace();
        }

        return result;
    }

    private static Iterator<Map.Entry<String, String>> generateIterator() {
        return engines.entrySet().iterator();
    }
}