package com.detzerg.sbadmin.Commands.Spigot;

import com.detzerg.sbadmin.Modules.OpControl.Opc;
import com.detzerg.sbadmin.Modules.Util.SUtil;
import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SOpctrlCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arg) {
        if (!s.equalsIgnoreCase("opc")){
            return true;
        }

        if (!sender.hasPermission("opc.command")){
            sender.sendMessage(SUtil.f("&c¿Que intentas hacer amigo?"));
            return true;
        }

        if (arg.length < 2){
            help(sender);
            return true;
        }


        switch (arg[0]){
            case "password":
                SpigotPlugin.getMain().getOpc().setHash(arg[1], false);
                System.out.println(SpigotPlugin.getMain().getOpc().getHash());
                break;
        }

        return true;
    }

    public void help(CommandSender sender){
        sender.sendMessage(SUtil.message("&7Usa:"));
        sender.sendMessage(SUtil.message("&8- &2/&aopc password &7<password>"));
    }
}
