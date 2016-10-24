package com.example.ddavi.prueba;

import com.example.ddavi.prueba.Processors.BaseConfig;
import com.example.ddavi.prueba.Processors.PureDataConfig;

/**
 * Created by oargueyo on 23/10/16.
 */


class MasterConfig {

    //Processors Adapter
    //PureDataConfig pdConfig;
    //FaustConfig faustConfig;
    //Creamos una clase abstracta padre que maneja la funcionalidades comunes de las hijas
    BaseConfig config;

    String procesador;

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public void setProcessorActivity(MainActivity context){
        switch (this.getProcesador()){
            case "Pd" :
                this.config.setContext(context);
                break;
            case "Faust" :
                break;
        }
    }

    public MasterConfig(String processor){
        this.setProcesador(processor);
        switch (this.getProcesador()){
            case "Pd" :
                this.config = new PureDataConfig();
                break;
            case "Faust" :
                break;
        }
    }

    public void resetPresets(){
        config.resetPresets();
        /*
        switch (this.getProcesador()){
            case "Pd":
                //((PureDataConfig) config).resetPresets();
                config.resetPresets();
        }
        */

    }

    public void setPreset(String name){
        config.setPreset(name);
        /*switch (this.getProcesador()){
            case "Pd":
                //Con esto tiro la herencia a la mierda, pero sino
                //necesito agregar una firma mas
                //((PureDataConfig) config).setPreset(name);

        }*/
    }
}
