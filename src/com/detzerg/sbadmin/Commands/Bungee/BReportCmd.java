package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Redis.RedisPlayer;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import com.sun.javafx.collections.MappingChange;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BReportCmd extends Command {
    private BungeePlugin main;
    private String server;
    public BReportCmd(BungeePlugin main) {
        super("breport");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer){
            return;
        }

        if (args.length<3){
            BUtil.say("El comando esta imcompleto, usa &d/breport &7<player> <victim> <reason>");
            return;
        }

        ProxiedPlayer reporter = main.getProxy().getPlayer(args[0]);
        ProxiedPlayer victim = main.getProxy().getPlayer(args[1]);


        if (victim!=null && reporter!=null){
            reportBungeeMode(reporter, victim.getDisplayName(), getReason(args, 2));
            return;
        }

        if (main.getC().redisMode()){
            if (reporter!=null && !reporter.hasPermission("report.command")){
                reporter.sendMessage(BUtil.f("&cNo tienes permisos para reportar jugadores"));
                return;
            }
            if (victim!=null){
                server = victim.getServer().getInfo().getName();
                reportRedisMode(args[0], args[1], getReason(args, 2), server);
                if (reporter!=null)
                    reporter.sendMessage(BUtil.f("&cEl jugador &f" + args[1] + " &c no esta conectado"));
            }
            else{
                main.getProxy().getPluginManager().dispatchCommand(main.getProxy().getConsole(),
                        "sync -b /bfind "+args[0]+" "+args[1]);
                main.getProxy().getScheduler().schedule(main, () -> {
                    RedisPlayer rsp = main.getReports().get(args[0]);
                    if (rsp!=null) {
                        server = rsp.getVictim_server();
                        reportRedisMode(args[0], args[1], getReason(args, 2), server);
                        if (reporter!=null)
                            reporter.sendMessage(BUtil.f("&7Reportaste a &c&l"+args[1]+"&7, sera revisado!"));
                        main.getReports().remove(args[0]);
                    }
                    else {
                        if (reporter!=null)
                            reporter.sendMessage(BUtil.f("&cEl jugador &f" + args[1] + " &c no esta conectado"));
                    }
                }, 5, TimeUnit.SECONDS);
            }
        }
    }

    private void reportRedisMode(String reporter, String victim, String reason, String server){
        broadcastToStaff(reporter, victim, server, reason);
    }

    private void reportBungeeMode(ProxiedPlayer reporter, String victim, String reason){

        ProxiedPlayer v =  main.getProxy().getPlayer(victim);
        if (v==null){
            reporter.sendMessage(BUtil.f("El jugador no esta conectado!"));
            return;
        }
        if (!reporter.hasPermission("report.command")){
            reporter.sendMessage(BUtil.f("&cNo tienes permisos para reportar jugadores"));
            return;
        }

        String serverToSend = v.getServer().getInfo().getName();
        broadcastToStaff(reporter.getDisplayName(), v.getDisplayName(), serverToSend, reason);
        reporter.sendMessage(BUtil.f("&7Reportaste a &c&l"+v.getDisplayName()+"&7, sera revisado!"));

    }

    private String getReason(String[]args, int init){
        String reason = "";
        for (int i = init; i < args.length; i++) {
            reason = reason + args[i] + " ";
        }
        return reason;
    }

    private void broadcastToStaff(String player, String victim, String server, String reason){
        for (ProxiedPlayer p: main.getProxy().getPlayers()){
            if (p.hasPermission("report.receive")){
                List<String> lines = main.getC().getConfig().getStringList("report_format_body");
                for (String l: lines){
                    l = l.replace("{player}", player)
                            .replace("{victim}", victim)
                            .replace("{server}", server)
                            .replace("{reason}", reason);
                    p.sendMessage(BUtil.message(l));
                }

                TextComponent clicker = new TextComponent();
                clicker.setText(main.getC().getConfig().getString("goto").replace("&", "§"));
                clicker.setHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(
                                main.getC().getConfig().getString("goto_hover").replace("&", "§")
                        ).create()));
                clicker.setClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/goto "+server));
                p.sendMessage(clicker);
                p.sendMessage(BUtil.message(" "));
                p.sendMessage(BUtil.message(main.getC().getConfig().getString("report_format_footer")));
            }
        }
    }
}
