package com.example.ddavi.prueba;

import android.util.Log;

import com.example.ddavi.prueba.Processors.BaseConfig;
import com.example.ddavi.prueba.Processors.BaseProcessor;
import com.example.ddavi.prueba.Processors.PureDataConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;

/**
 * Created by oargueyo on 23/10/16.
 */


public class MasterConfig {

    //Processors Adapter
    //PureDataConfig pdConfig;
    //FaustConfig faustConfig;
    //Creamos una clase abstracta padre que maneja la funcionalidades comunes de las hijas
    public BaseProcessor config;

    JSONObject processor_list;

    String procesador;

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public void setProcessorActivity(MainActivity context){
        this.config.setContext(context);
    }

    public MasterConfig(String processor){
        this.setProcesador(processor);
    }


    public MasterConfig(JSONObject configFile){
        try {
            this.procesador = configFile.getString("processor");
            String nameClassProcessor = configFile.getJSONObject("processors").getString(this.procesador);
            Class<?> clase = Class.forName(nameClassProcessor);
            Constructor constructor = clase.getConstructor();
            this.config = (BaseProcessor) constructor.newInstance();
            String msg = "";
            Log.i(String.format("P seleccionado es%s", nameClassProcessor), msg);

        }
        catch(Exception a){
            a.printStackTrace();
        }

    }

    public void resetPresets(){
        config.resetPresets();
    }

    public void setPreset(String name){
        config.setPreset(name , (float)1);
    }

    public void sendValue(String name , Float val){
        config.sendValue(name , val);
    }
}
