package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.presentation_layer.activity.MainActivity;

/**
 * Inicializa y configura la herramienta de procesamiento de audio.
 * Debe ofrecer un metodo para liberar todos los recursos utilizados.
 */
public interface Config {

    /**
     * Libera los recursos utilizados.
     */
    void cleanup();

    /**
     * Retorna una instancia de procesador lista para ser utilizada.
     *
     * @return procesador inicializado.
     */
    Processor getProcessor();

    /**
     * Indica si esta en ejecucion.
     *
     * @return true si el servicio esta en ejecucion.
     */
    boolean isServiceRunning();

    /**
     * Utiliza el contexto de la aplicacion para inicializar la herramienta.
     * El contexto es requerido para el correcto funcionamiento de este objeto.
     *
     * @param c Instancia del contexto como MainActivity.
     */
    void setContext(MainActivity c);

    /**
     * Comienza la reproduccion de audio.
     */
    void startAudio();

    /**
     * Detiene la reproduccion de audio.
     */
    void stopAudio();
}
