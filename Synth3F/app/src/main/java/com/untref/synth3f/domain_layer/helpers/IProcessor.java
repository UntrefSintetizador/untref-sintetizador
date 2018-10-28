package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;

/**
 * Acceso para el uso de la herramienta de procesamiento del sonido.
 */
public interface IProcessor {

    /**
     * Elimina todos los patches recibidos.
     *
     * @param patches Patches a eliminar.
     */
    void clear(Patch[] patches);

    /**
     * Connecta los patches entre el inlet y outlet recibidos.
     *
     * @param connection Datos de la conexion.
     */
    void connect(Connection connection);

    /**
     * Crea una instancia del tipo de patch recibido.
     *
     * @param type    Codigo del tipo de patch.
     * @param patchId ID del patch a crear.
     */
    void createPatch(String type, int patchId);

    /**
     * Elimina el patch y sus conexiones.
     *
     * @param patch Patch a eliminar.
     */
    void delete(Patch patch);

    /**
     * Desconecta los patches entre el inlet y outlet recibidos.
     *
     * @param connection Datos de la conexion.
     */
    void disconnect(Connection connection);

    /**
     * Asigna el valor del parametro solicitado al patch en la herramienta.
     *
     * @param name  Nombre de la variable a modificar en el formato
     *              "x_" + patch.getTypeName() + "_" + Integer.toString(patch.getId()) + "_" + name
     * @param value Valor a asignar.
     */
    void sendValue(String name, Float value);
}