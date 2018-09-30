package com.detzerg.sbadmin.Modules.Config;

import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SpigotConfig {
    private FileConfiguration config;
    private SpigotPlugin main;

    public SpigotConfig(SpigotPlugin context) {
        this.main = context;
        if(!context.getDataFolder().exists()){
            context.getDataFolder().mkdir();
        }

        File file = new File(context.getDataFolder(), "config.yml");

        if (!file.exists()){
            context.saveResource("config.yml", false);
            config = YamlConfiguration.loadConfiguration(file);
            config.options().copyDefaults(true);
        }else{
            this.config = YamlConfiguration.loadConfiguration(file);
        }

    }

    public FileConfiguration getConfig() {
        return config;
    }

    public boolean useDb(){
        return config.getBoolean("use_db");
    }

    public boolean isValid(){
        if (config.getString("server_name").trim().equals(""))
            return false;
        if (config.getInt("server_id") == 0)
            return false;
        return true;
    }
}
