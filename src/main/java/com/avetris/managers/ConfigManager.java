package com.avetris.managers;

import com.avetris.models.Config;
import com.avetris.utils.FileManager;
import com.google.gson.Gson;

public class ConfigManager {

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
            content = FileManager.readFile("config.json");
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
        FileManager.saveFile("config.json", content);
    }    

    public void setConfig(Config config) {
        this.config = config;
    }
}
