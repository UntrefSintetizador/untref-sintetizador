package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Almacena la informacion de los patches y sus conexiones.
 */
public class PatchGraphManager {

    private int maxId;
    private Map<Integer, Patch> patchMap;
    private Map<Integer, Connection> connectionMap;

    public PatchGraphManager() {
        clear();
    }

    /**
     * Almacena un nuevo patch y le asigna un ID.
     *
     * @param patch Patch a almacenar.
     * @return ID asignada al patch.
     */
    public Integer addPatch(Patch patch) {
        this.maxId += 1;
        patch.setId(maxId);
        patchMap.put(maxId, patch);
        return maxId;
    }

    /**
     * Elimina la informacion que contiene
     */
    public void clear() {
        this.maxId = 0;
        this.patchMap = new HashMap<>();
        this.connectionMap = new HashMap<>();
    }

    /**
     * Conecta los patches entre el inlet y outlet recibidos.
     * Incluye la conexion en los conjuntos de entrada y salida de los patches correspondientes.
     *
     * @param sourcePatch  ID del patch de salida.
     * @param sourceOutlet ID del la salida del patch.
     * @param targetPatch  ID del patch de entrada.
     * @param targetInlet  ID del la entrada del patch.
     * @return Conexion creada con los valores recibidos y una ID.
     */
    public Connection connect(int sourcePatch, int sourceOutlet, int targetPatch, int targetInlet) {
        this.maxId += 1;
        Connection connection = new Connection();
        connection.setId(maxId);
        connection.setSourcePatch(sourcePatch);
        connection.setSourceOutlet(sourceOutlet);
        connection.setTargetPatch(targetPatch);
        connection.setTargetInlet(targetInlet);
        connectionMap.put(maxId, connection);
        patchMap.get(sourcePatch).addOutputConnection(connection);
        patchMap.get(targetPatch).addInputConnection(connection);
        return connection;
    }

    /**
     * Elimina una conexion.
     * Remueve la conexion de los conjuntos de entrada y salida de los patches correspondientes.
     *
     * @param connectionId ID de la conexion a eliminar.
     * @return Conexion eliminada.
     */
    public Connection disconnect(int connectionId) {
        Connection connection = connectionMap.remove(connectionId);
        patchMap.get(connection.getSourcePatch()).removeOutputConnection(connection);
        patchMap.get(connection.getTargetPatch()).removeInputConnection(connection);
        return connection;
    }

    /**
     * Retorna la conexion con la ID solicitada.
     *
     * @param connectionId ID de la conexion a solicitada.
     * @return Conexion con la ID solicitada.
     */
    public Connection getConnection(int connectionId) {
        return connectionMap.get(connectionId);
    }

    /**
     * Retorna todas sus conexiones.
     *
     * @return Collection con todas las conexiones.
     */
    public Collection<Connection> getConnections() {
        return connectionMap.values();
    }

    /**
     * Retorna el patch con la ID solicitada.
     *
     * @param patchId ID del patch a obtener.
     * @return Patch con la ID solicitada.
     */
    public Patch getPatch(int patchId) {
        return patchMap.get(patchId);
    }

    /**
     * Retorna todos sus patches.
     *
     * @return Collection con todos los patches.
     */
    public Collection<Patch> getPatches() {
        return patchMap.values();
    }

    /**
     * Elimina el patch con la ID solicitada.
     * Remueve las conexiones de los conjuntos de entrada y salida de los patches correspondientes.
     *
     * @param patchId ID del patch a eliminar.
     * @return Patch eliminado.
     */
    public Patch removePatch(int patchId) {
        Patch patch = patchMap.remove(patchId);
        for (Connection connection : patch.getInputConnections()) {
            connectionMap.remove(connection.getId());
            patchMap.get(connection.getSourcePatch()).removeOutputConnection(connection);
        }
        for (Connection connection : patch.getOutputConnections()) {
            connectionMap.remove(connection.getId());
            patchMap.get(connection.getTargetPatch()).removeInputConnection(connection);
        }
        return patch;
    }

    public Patch[] removeAllPatches(){
        Patch[] patchesToRemove = new Patch[this.patchMap.size()];
        int positionToAddPatch = 0;
        Iterator it = this.patchMap.keySet().iterator();
        while(it.hasNext()) {
            Integer key = (Integer) it.next();
            Patch patch = this.patchMap.get(key);
            patchesToRemove[positionToAddPatch] = patch;
            positionToAddPatch++;
        }
        for (int i = 0; i < patchesToRemove.length; i++){
            Patch patch = patchesToRemove[i];
            this.removePatch(patch.getId());
        }
        return patchesToRemove;
    }

}
