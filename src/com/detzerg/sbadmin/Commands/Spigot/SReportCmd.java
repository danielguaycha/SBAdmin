package com.detzerg.sbadmin.Commands.Spigot;

import com.detzerg.sbadmin.Modules.Util.SUtil;
import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SReportCmd implements CommandExecutor {
    private SpigotPlugin context;
    public SReportCmd(SpigotPlugin context){
        this.context = context;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!s.equalsIgnoreCase("report"))
            return true;
        Player p;
        if (sender instanceof Player){
            p = (Player) sender;
            if (args.length == 0) {
                help(p);
                return true;
            }
            String victim = args[0];
            if (victim.length() < 3 ) {
                p.sendMessage(SUtil.message("&c"+args[0]+ "&f no es un nick valido!"));
                return true;
            }
            if (victim.equals(p.getDisplayName())){
                p.sendMessage(SUtil.message("&cNo te puedes reportar a ti mismo &e._."));
                return true;
            }
            context.getRp().menu(p, victim);
        }
        else{
            sender.sendMessage(SUtil.message("&cEste comando solo puede ser ejecutado por un jugador!"));
        }
        return true;
    }

    public void help(Player p){
        p.sendMessage(SUtil.message("&fUsa: &c/report &3<nick>"));
    }
}
