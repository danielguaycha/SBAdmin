package com.detzerg.sbadmin.Modules.Reporter;

import com.detzerg.sbadmin.Modules.Util.SUtil;
import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;

public class ReportConfig {

    private FileConfiguration reportCfg;
    private ItemStack[] items;

    public ReportConfig(SpigotPlugin main){
        File fileReportCfg = new File(main.getDataFolder(), "report.yml");
        if (!fileReportCfg.exists())
            main.saveResource("report.yml", false);
        reportCfg = YamlConfiguration.loadConfiguration(fileReportCfg);
        reportCfg.options().copyDefaults(true);
        items = new ItemStack[reportCfg.getInt("HackMenuSlots")];
        items();
    }

    private void items(){
        if (this.reportCfg.getConfigurationSection("TYPES-HACKS") !=null){
            for (String hack : this.reportCfg.getConfigurationSection("TYPES-HACKS").getKeys(false)) {
                items[this.cfg().getInt("TYPES-HACKS."+hack+".slot")] = SUtil.getItem(
                        Material.getMaterial(reportCfg.getString("TYPES-HACKS."+hack+".item")), 1, 0,
                        reportCfg.getString("TYPES-HACKS."+hack+".name"),
                        Arrays.asList(new String[] {
                                reportCfg.getString("TYPES-HACKS."+hack+".lore")})
                );
            }
        }
    }

    public ItemStack[] getItems(){
        return items;
    }

    public FileConfiguration cfg(){
        return reportCfg;
    }

}
