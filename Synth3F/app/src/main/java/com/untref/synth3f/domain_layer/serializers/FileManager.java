package com.untref.synth3f.domain_layer.serializers;

import android.content.Context;

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

    private static final String FILE_FOLDER = "saves";

    public static void writeOnFile(Context context, String filename, String data) throws IOException {
        File file = new File(new File(context.getFilesDir(), FileManager.FILE_FOLDER), filename);
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

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        File fl = new File(new File(context.getFilesDir(), FILE_FOLDER), filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    public static List<String> getFilenameList(Context context){
        File filesDir = new File(context.getFilesDir(), FILE_FOLDER);
        filesDir.mkdirs();
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
}
