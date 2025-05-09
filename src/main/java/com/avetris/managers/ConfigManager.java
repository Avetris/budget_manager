package com.avetris.managers;

import javax.swing.JTextField;

import com.avetris.models.Config;
import com.avetris.utils.FileManager;
import com.google.gson.Gson;

public class ConfigManager {

    private final String CONFIG_PATH = FileManager.getFilePath("config.json");

    private static ConfigManager _instance;

    private Config config;

    public static ConfigManager getInstance() {
        if (_instance == null) {
            _instance = new ConfigManager();
        }
        return _instance;
    }
    

    private ConfigManager() {
        readConfig();
    }

    private void readConfig() {
        String content = "";
        boolean exists = false;
        try {
            content = FileManager.readFile(CONFIG_PATH, "{}");
            exists = true; 
        } catch (Exception exception) {
            exists = false;
        }
        Gson gson = new Gson();  
        config = gson.fromJson(content, Config.class);
        if(!exists) {
            saveConfig();
        }
    }

    public Config getConfig() {
        return config;
    }

    public void saveConfig() {        
        String content = new Gson().toJson(config);
        FileManager.saveFile(CONFIG_PATH, content);
    }    

    public void setConfig(Config config) {
        this.config = config;
        saveConfig();
    }
}
