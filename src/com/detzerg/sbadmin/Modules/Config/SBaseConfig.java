package com.detzerg.sbadmin.Modules.Config;

import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public abstract class SBaseConfig {

    protected final SpigotPlugin plugin;
    private final File file;
    protected FileConfiguration config;

    public SBaseConfig(SpigotPlugin plugin, String filename, boolean copy) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), (filename==null)?"config.yml":filename);
        init(copy);
    }

    public SBaseConfig(SpigotPlugin plugin, String filename){
        this(plugin, filename, true);
    }

    private void init(boolean copy){
        if (!file.exists()){
            if (copy) {
                plugin.saveResource(file.getName(), false);
                this.config = YamlConfiguration.loadConfiguration(file);
            }
            else {
                try {
                    file.createNewFile();
                    this.config = YamlConfiguration.loadConfiguration(file);
                    setDefault();
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            this.config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void setDefault() {

    }

    public FileConfiguration get(){
        return config;
    }

    public void reload() {
        try {
            config.load(file);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        config.save(file);
    }
}
