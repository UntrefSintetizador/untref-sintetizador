package com.untref.synth3f.domain_layer.serializers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.untref.synth3f.domain_layer.helpers.PatchGraphManager;
import com.untref.synth3f.entities.Patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Persiste y recupera los patches.
 */
public class JSONSerializer {

    public static final String FILE_FOLDER = "saves";

    /**
     * Crea un PatchGraphManager con los datos del archivo.
     *
     * @param context  Contexto requerido para obtener la ubicacion de los archivos.
     * @param filename Nombre del Archivo.
     * @return PatchGraphManager inicializado con los datos del archivo.
     */
    public PatchGraphManager load(Context context, String filename) {
        PatchGraphManager patchGraphManager;
        try {
            String json = getStringFromFile(context, filename);
            Log.i("json", json);
            Gson gson = new GsonBuilder().registerTypeAdapter(Patch.class, new InterfaceAdapter<Patch>()).create();
            Type type = new TypeToken<PatchGraphManager>() {
            }.getType();
            patchGraphManager = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            patchGraphManager = new PatchGraphManager();
        }
        return patchGraphManager;
    }

    /**
     * Crea o sobrescribe el archivo con los datos del PatchGraphManager recibido.
     *
     * @param context           Contexto requerido para obtener la ubicacion de los archivos.
     * @param patchGraphManager PatchGraphManager a guardar.
     * @param filename          Nombre del Archivo.
     */
    public void save(Context context, PatchGraphManager patchGraphManager, String filename) {

        Gson gson = new GsonBuilder().registerTypeAdapter(Patch.class, new InterfaceAdapter<Patch>()).create();
        Type type = new TypeToken<PatchGraphManager>() {
        }.getType();
        String json = gson.toJson(patchGraphManager, type);
        Log.i("json", json);

        try {
            File file = new File(new File(context.getFilesDir(), FILE_FOLDER), filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromFile(Context context, String filePath) throws Exception {
        File fl = new File(new File(context.getFilesDir(), FILE_FOLDER), filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
