package com.avetris.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.avetris.models.Config;
import com.avetris.models.Task;
import com.google.gson.Gson;

public class FileManager {
    public static Task[] readTasks() throws Exception {
        String content = readFile("tasks.json");
        Gson gson = new Gson();  
        return gson.fromJson(content, Task[].class);
    }

    public static void saveTasks(Task[] tasks) {
        String content = new Gson().toJson(tasks);
        saveFile("tasks.json", content);
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
        if(!file.exists()) {
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

    public static String readFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File not found: " + filePath);
        }
        return new String(Files.readAllBytes(file.toPath()));
    }
    
    public static void removeFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
