package com.detzerg.sbadmin.Modules.Executor;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class ExecutorBungee {

    private BungeePlugin bp = BungeePlugin.getMain();

    public ExecutorBungee(ExecutorData executorData) {

        boolean canExec;

        if((executorData.getServer_name().equals(bp.getC().getConfig().getString("server_name"))
                && executorData.getServer_id() == bp.getC().getConfig().getInt("server_id"))) {
            canExec = true;
        }
        else if (executorData.getServer_id() == -1 && executorData.getServer_name().equals("bungees")) {
            canExec = true;
        }
        else {
            canExec = false;
        }

        if (!canExec)
            return;

        switch (executorData.getSource()){
            case "web":
                if(!isAllowRol(executorData.getRol_id())) {
                    System.out.println("Rol no permitido");
                    return;
                }
                if(!isAllowUser(executorData.getPlayer())) {
                    System.out.println("Jugador no permitido");
                    return;
                }
                executeCmds(executorData.getCommands());
                break;
            case "bungeeConsole":
                executeCmds(executorData.getCommands());
                break;
            case "spigotConsole":
                ProxiedPlayer player = bp.getProxy().getPlayer(executorData.getPlayer());
                if(!isAllowUser(executorData.getPlayer())) {
                    if (player.isConnected())
                        player.sendMessage(BUtil.message("No tienes permitido ejectuar comandos!"));
                    return;
                }
                executeCmds(executorData.getCommands());
                break;
        }
    }

    private void executeCmds(ArrayList<String> commands) {
        for(String cmd : commands){
            if(this.isAllowCmd(cmd)) {
                BUtil.say("Ejecutando &d/"+cmd);
                bp.getProxy().getPluginManager().dispatchCommand(bp.getProxy().getConsole(), cmd);
            }
        }
    }

    private Boolean isAllowRol(String rol){
        List l =  bp.getC().getConfig().getList("allowed_roles");

        if (l.size()==0)
            return true;

        for (Object o : l){
            if (o.toString().equals(rol)){
                return true;
            }
        }

        return false;
    }

    private Boolean isAllowUser(String nickname){
        List u = bp.getC().getConfig().getList("denied_users");
        if(u.size() == 0){
            return true;
        }
        for (Object us : u){
            if (us.toString().equals(nickname)){
                return false;
            }
        }
        return true;
    }

    private Boolean isAllowCmd(String cmd){
        List c = bp.getC().getConfig().getList("denied_commands");

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
