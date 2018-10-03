package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Env;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BSyncCmd extends Command {
    BungeePlugin main;
    public BSyncCmd(BungeePlugin main) {
        super("sync");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("sync.command")){
            sender.sendMessage(BUtil.message("&cNo tienes permiso para ejecutar este comando"));
        }else{
            main.getProxy().getScheduler().runAsync(main, ()->{

                if (args.length>0 && args[0].startsWith("/") && !args[0].substring(1).trim().equals("")){
                    publishCmdSpigot(args);
                    sender.sendMessage(BUtil.message("&dComando Sincronizado!"));
                }
                else if(args.length >0 && args[0].startsWith("-") && !args[0].substring(1).trim().equals("")){
                    String subCmd = args[0].substring(1).trim();
                    switch (subCmd){
                        case "b":
                            if (args.length>1 && args[1].startsWith("/") && !args[1].substring(1).trim().equals("")){
                                publishCmdBungees(args);
                            }else {
                                printHelp(sender);
                            }
                            break;
                        default:
                            printHelp(sender);
                            break;
                    }
                }
                else{
                    printHelp(sender);
                }

            });
        }
    }

    private void publishCmdSpigot(String args[]){
        JSONArray cmds = new JSONArray();
        cmds.add(joinCmd(args, 0));
        JSONObject json = new JSONObject();
        json.put("player", "Console "+main.getC().getConfig().getString("server_name"));
        json.put("player_id", main.getC().getConfig().getInt("server_id"));
        json.put("server_id", 0);
        json.put("server_name", "spigots");
        json.put("rol_id", "BungeeCord");
        json.put("source", "bungeeConsole");
        json.put("commands", cmds);

        main.getMqtt().publish(json.toJSONString(), Env.topicExec);
    }

    private void publishCmdBungees(String[] args){
        JSONArray cmds = new JSONArray();
        cmds.add(joinCmd(args, 1));
        JSONObject json = new JSONObject();
        json.put("player", "Console "+main.getC().getConfig().getString("server_name"));
        json.put("player_id", main.getC().getConfig().getInt("server_id"));
        json.put("server_id", -1);
        json.put("server_name", "bungees");
        json.put("rol_id", "BungeeCord");
        json.put("source", "bungeeConsole");
        json.put("commands", cmds);

        main.getMqtt().publish(json.toJSONString(), Env.topicExec);
    }

    private void printHelp(CommandSender sender){
        sender.sendMessage(BUtil.message("&7Usa: "));
        sender.sendMessage(BUtil.message("&7- &c/sync&8 °°&e/command&8°° &7:: Sync all Spigot Servers"));
        sender.sendMessage(BUtil.message("&7- &c/sync -b &8°°&e/command&8°° &7:: Sync all Bungee Servers"));
    }

    private String joinCmd(String[] args, int start){
        String cmdSync="";
        for (int i=start; i<args.length; i++){
            if (i==start) {
                if (args[i].startsWith("/")) {
                    cmdSync += args[i].substring(1) + " ";
                }else{
                    cmdSync += args[i]+ " ";
                }
            }
            else if (i==args.length-1)
                cmdSync +=args[i];
            else
                cmdSync +=args[i]+" ";
        }
        return cmdSync.trim();
    }
}
