package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import java.util.List;

public class BReportCmd extends Command {
    private BungeePlugin main;
    public BReportCmd(BungeePlugin main) {
        super("breport");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof  ProxiedPlayer){
            ProxiedPlayer reporter = (ProxiedPlayer)sender;
            if (args.length < 2){
                reporter.sendMessage(BUtil.f("Usa: &6/breport &7<player> <reason>"));
                return;
            }
            report(reporter, args[0], getReason(args, 1));
            return;
        }

        if (args.length<3){
            BUtil.say("El comando esta imcompleto, usa &d/breport &7<player> <victim> <reason>");
            return;
        }

        ProxiedPlayer reporter = main.getProxy().getPlayer(args[0]);
        if (reporter == null){
            BUtil.say("El jugador "+reporter+" esta desconectado!");
            return;
        }

        report(reporter, args[1], getReason(args, 2));
    }

    private void report(ProxiedPlayer reporter, String victim, String reason){

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
