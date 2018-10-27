package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.presentation_layer.activity.MainActivity;

/**
 * Inicializa y configura la herramienta de procesamiento del sonido.
 * Debe ofrecer un metodo para liberar todos los recursos utilizados.
 */
public interface IConfig {

    /**
     * Libera los recursos utilizados.
     */
    void cleanup();

    /**
     * Retorna una instancia IProcessor lista para ser utilizada.
     *
     * @return IProccesor inicializado.
     */
    IProcessor getProcessor();

    /**
     * Indica si esta en ejecucion.
     *
     * @return true si el servicio esta en ejecucion.
     */
    boolean isServiceRunning();

    /**
     * Utiliza el contexto para inicializar la herramienta. Puede ser requerido para funcionar.
     *
     * @param c Instancia de MainActivity.
     */
    void setContext(MainActivity c);

    /**
     * Comienza la reproduccion del sonido.
     */
    void startAudio();

    /**
     * Detiene la reproduccion del sonido.
     */
    void stopAudio();
}
