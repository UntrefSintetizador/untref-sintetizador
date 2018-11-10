package com.untref.synth3f.domain_layer.serializers;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FILE_FOLDER = "synth3f_saves";

    public static void writeOnFile(Context context, String filename, String data) throws IOException {
        File file = new File(getFileFolder(context), filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static String getStringFromFile(Context context, String filename) throws Exception {
        File fl = new File(getFileFolder(context), filename);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    public static List<String> getFilenameList(Context context) {
        File filesDir = getFileFolder(context);
        String name;
        List<String> list = new ArrayList<>();
        for (File file : filesDir.listFiles()) {
            if (file.isFile()) {
                name = file.getName();
                list.add(name);
            }
        }
        return list;
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    private static File getFileFolder(Context context) {
        String state = Environment.getExternalStorageState();
        File fileFolder = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            fileFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), FILE_FOLDER);
            //Si el directorio no existe y no se puede crear usar directorio privado
            if (!fileFolder.isDirectory() && !fileFolder.mkdir()) {
                fileFolder = null;
            }
        }
        if (fileFolder == null) {
            fileFolder = new File(context.getFilesDir(), FILE_FOLDER);
            if (!fileFolder.isDirectory()) {
                fileFolder.mkdir();
            }
        }
        return fileFolder;
    }
}
