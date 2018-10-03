package com.detzerg.sbadmin.Modules.Executor;

import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ExecutorSpigot {
    private SpigotPlugin sp = SpigotPlugin.getMain();
    public ExecutorSpigot(ExecutorData executorData) {

        Boolean canExec;

        if((executorData.getServer_name().equals(sp.getCfg().getConfig().getString("server_name"))
                && executorData.getServer_id() == sp.getCfg().getConfig().getInt("server_id")))
            canExec = true;
        else if (executorData.getServer_id() == 0 && executorData.getServer_name().equals("spigots"))
            canExec = true;
        else
            canExec = false;

        if (!canExec)
            return;

        switch (executorData.getSource()){
            case "bungeeConsole":
                for(String cmd : executorData.getCommands()){
                    if(this.isAllowCmd(cmd)) {
                        sp.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&5Ejecutando &d/"+cmd));
                        if(Bukkit.isPrimaryThread()) {
                            sp.getServer().dispatchCommand(sp.getServer().getConsoleSender(), cmd);
                        }else {
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    sp.getServer().dispatchCommand(sp.getServer().getConsoleSender(), cmd);
                                }
                            }.runTask(sp);
                        }
                    }
                }
                break;
        }

    }
    private Boolean isAllowCmd(String cmd){
        List c = sp.getCfg().getConfig().getList("denied_commands");

        if (c.size() == 0)
            return true;

        for (Object cm : c){
            if (cm.toString().equals(cmd) || cmd.split(" ")[0].equals(cm)){
                return false;
            }
        }
        return true;
    }
}
