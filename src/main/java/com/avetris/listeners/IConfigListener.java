package com.avetris.listeners;

import com.avetris.models.Config;

public interface IConfigListener {
    public void onSubmit(Config config);  
    public Config getConfig();
    public String GetIcon();
}
