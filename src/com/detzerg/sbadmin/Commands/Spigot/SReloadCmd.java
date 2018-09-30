package com.detzerg.sbadmin.Commands.Spigot;


import com.detzerg.sbadmin.Modules.Util.SUtil;
import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SReloadCmd implements CommandExecutor {
    private SpigotPlugin sp;
    public SReloadCmd(SpigotPlugin sp){
        this.sp = sp;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arg) {

        if (!s.equalsIgnoreCase("sadmin"))
            return false;

        if (arg.length>0){
            if (arg[0].equalsIgnoreCase("reload")){
                if (sender.hasPermission("reload.command")) {
                    sp.onDisable();
                    sp.load();
                    sender.sendMessage(ChatColor.GREEN + "Reload Conpleto");
                    return true;
                }else {
                    sender.sendMessage(SUtil.message("&cNo tienes permiso para hacer esto!"));
                    return true;
                }
            }
        }

        sender.sendMessage(ChatColor.GRAY+"Usa: "+ChatColor.RED+"/sadmin "+ChatColor.WHITE+"reload");
        return true;
    }
}
