package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BAdminCmd extends Command {
    private BungeePlugin main;
    public BAdminCmd(BungeePlugin main) {
        super("badmin");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!sender.hasPermission("reload.command")){
            sender.sendMessage(BUtil.f("&cNo tienes permiso para ejecutar esto"));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(BUtil.f("&cUsa /badmin reload"));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")){
            main.onDisable();
            main.load();
            sender.sendMessage(BUtil.f("&aPlugin reloaded successful!"));
        }
    }
}
