package com.detzerg.sbadmin.Modules.Config;

import com.detzerg.sbadmin.BungeePlugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeConfig {
    private File file;
    private Configuration config;
    public BungeeConfig(){
        create();
    }

    private void create(){
        if(!BungeePlugin.getMain().getDataFolder().exists()){
            BungeePlugin.getMain().getDataFolder().mkdir();
        }
        file = new File(BungeePlugin.getMain().getDataFolder().getPath(), "config.yml");

        try  {
            if (!file.exists()) {
                InputStream in = BungeePlugin.getMain().getResourceAsStream("bconfig.yml");
                Files.copy(in, file.toPath());
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }else{
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public boolean isValid(){
        if (config.getString("server_name").trim().equals(""))
            return false;
        if (config.getInt("server_id") == 0)
            return false;
        return true;
    }

    public boolean useDb(){
        return config.getBoolean("use_db");
    }

    public boolean redisMode(){
        return config.getBoolean("bungee_redis");
    }
}
