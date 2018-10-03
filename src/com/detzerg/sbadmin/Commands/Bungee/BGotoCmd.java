package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BGotoCmd extends Command {

    BungeePlugin main;
    public BGotoCmd(BungeePlugin main) {
        super("goto");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] arg) {
        if(sender instanceof ProxiedPlayer){
            if (arg.length == 0)
                return;
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!p.hasPermission("report.goto"))
                return;
            try {
                ServerInfo sv = main.getProxy().getServerInfo(arg[0]);
                if (sv != p.getServer().getInfo()) {
                    ServerInfo server = main.getProxy().getServerInfo(arg[0]);
                    if (server!=null) {
                        p.connect(server);
                    }else{
                        p.sendMessage(BUtil.f("&cSorry! no te podemos llevar :("));
                    }
                }else{
                    p.sendMessage(BUtil.message(" "));
                    p.sendMessage(BUtil.message("&aYa estas en este server!"));
                }
            }catch (Exception e){
                p.sendMessage(BUtil.message("Por alguna razón no podemos llevarte a este server!"));
            }

        }else {
            BUtil.say("Este comando solo puede ser ejecutado por jugadores");
        }
    }
}
