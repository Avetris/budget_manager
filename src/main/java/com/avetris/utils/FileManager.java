package com.avetris.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileManager {
    static final String PATH = "/Presupuestos/data/";

    public static String getFilePath(String filePath) {
        return System.getProperty("user.home") + PATH + filePath;
    }

    public static String[] getFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return new String[0];
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return new String[0];
        }
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

    public static void saveFile(String filePath, String content) {
        File file = new File(filePath);
        if(!file.exists() && file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        } else {
            file.delete();
        }
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath, String emptyContent) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            saveFile(filePath, emptyContent);
            return emptyContent;
        }
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }
    
    public static void removeFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
