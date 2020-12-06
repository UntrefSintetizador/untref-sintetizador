package com.untref.synth3f.domain_layer.helpers;

import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Almacena patches y sus conexiones. Permite conectar y desconectar patches.
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
     */
    public void addPatch(Patch patch) {
        maxId += 1;
        patch.setId(maxId);
        patchMap.put(maxId, patch);
    }

    /**
     * Elimina toda la informacion almacenada. Reinicia su estado.
     */
    public void clear() {
        maxId = 0;
        patchMap = new HashMap<>();
        connectionMap = new HashMap<>();
    }

    /**
     * Conecta una salida de un patch con una entrada de otro patch.
     *
     * @param sourcePatch  ID del patch de salida.
     * @param sourceOutlet ID del conector de salida del patch.
     * @param targetPatch  ID del patch de entrada.
     * @param targetInlet  ID del conector de entrada del patch.
     * @return la conexion resultante con un nuevo ID asignado.
     */
    public Connection connect(int sourcePatch, int sourceOutlet, int targetPatch,
                              int targetInlet) {
        maxId += 1;
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
     * Dado un ID de conexion, desconecta a los patches involucrados en dicha conexion.
     *
     * @param connectionId ID de la conexion a eliminar.
     */
    public void disconnect(int connectionId) {
        Connection connection = connectionMap.remove(connectionId);
        patchMap.get(connection.getSourcePatch()).removeOutputConnection(connection);
        patchMap.get(connection.getTargetPatch()).removeInputConnection(connection);
    }

    /**
     * Retorna la conexion con la ID solicitada.
     *
     * @param connectionId ID de la conexion a obtener.
     * @return la conexion con la ID solicitada.
     */
    public Connection getConnection(int connectionId) {
        return connectionMap.get(connectionId);
    }

    /**
     * Retorna todas sus conexiones.
     *
     * @return un objeto Collection con todas las conexiones.
     */
    public Collection<Connection> getConnections() {
        return connectionMap.values();
    }

    /**
     * Retorna el patch con la ID solicitada.
     *
     * @param patchId ID del patch a obtener.
     * @return el patch con la ID solicitada.
     */
    public Patch getPatch(int patchId) {
        return patchMap.get(patchId);
    }

    /**
     * Retorna todos sus patches.
     *
     * @return todos los patches almacenados.
     */
    public Collection<Patch> getPatches() {
        return patchMap.values();
    }

    /**
     * Elimina el patch con la ID solicitada.
     * Si el patch a eliminar esta conectado con otros patches, se eliminaran dichas conexiones
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

    /**
     * Elimina todos los patches almacenados, junto con sus conexiones.
     *
     * @return todos los patches eliminados.
     */
    public Patch[] removeAllPatches(){
        Patch[] patchesToRemove = new Patch[patchMap.size()];
        int positionToAddPatch = 0;
        for (Integer key : patchMap.keySet()) {
            Patch patch = patchMap.get(key);
            patchesToRemove[positionToAddPatch] = patch;
            positionToAddPatch++;
        }
        for (Patch patch : patchesToRemove) {
            removePatch(patch.getId());
        }
        return patchesToRemove;
    }

}
