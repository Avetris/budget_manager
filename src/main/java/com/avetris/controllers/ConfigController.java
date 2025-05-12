package com.avetris.controllers;

import com.avetris.listeners.IConfigListener;
import com.avetris.managers.ConfigManager;
import com.avetris.models.Config;
import com.avetris.ui.views.ConfigTab;

public class ConfigController implements IConfigListener {

    private ConfigTab view;

    public ConfigController(ConfigTab view) {
        this.view = view;
        this.view.addListener(this);
        this.view.updateView();
    }

    public void onSubmit(Config newConfig) {
        ConfigManager.getInstance().setConfig(newConfig);
    }

    public Config getConfig() {
        return ConfigManager.getInstance().getConfig();
    }

    public String GetIcon() {
        return ConfigManager.getInstance().getLogoPath();
    }
}
