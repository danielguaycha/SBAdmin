package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Env;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BFindCmd extends Command {
    private BungeePlugin main;
    private JSONArray players;
    public BFindCmd(BungeePlugin main) {
        super("bfind");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] arg) {

        if (!sender.hasPermission("find.command")){
            sender.sendMessage(BUtil.f("&cNo tienes permisos para hacer esto!"));
            return;
        }

        if(arg.length == 2){
            ProxiedPlayer victim = main.getProxy().getPlayer(arg[1]);
            if (victim!=null) {
                JSONObject json = new JSONObject();
                json.put("Origin", main.getProxy().getConfig().getUuid());
                json.put("Victim", victim.getDisplayName());
                json.put("Victim_Server", victim.getServer().getInfo().getName());
                json.put("WhoFind", arg[0]);

                main.getMqtt().publish(json.toJSONString(), Env.topicRedis);
            }else {
                System.out.println(arg[1]+" no esta en este server");
            }
        }
    }
}
