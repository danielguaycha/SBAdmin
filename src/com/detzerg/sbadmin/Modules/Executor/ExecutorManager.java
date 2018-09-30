package com.detzerg.sbadmin.Modules.Executor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class ExecutorManager {
    private String type;
    private ArrayList<String> cmds;

    private int player_id;
    private String player;
    private int server_id;
    private String server_name;
    private String rol_id;
    private String source;

    public ExecutorManager(String type, String stringJson){
        this.type = type;
        cmds = new ArrayList<>();
        processJson(stringJson);

        if (type.equals("Bungee")){
            new ExecutorBungee (new ExecutorData(player_id, player, server_id, server_name,
                    rol_id, source, cmds));
        }

        else if (type.equals("Spigot")){
            new ExecutorSpigot (new ExecutorData(player_id, player, server_id, server_name,
                    rol_id, source, cmds));
        }
    }

    private void processJson(String stringJson){
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(stringJson);
            this.player_id = Integer.parseInt(json.get("player_id").toString());
            this.player = json.get("player").toString();
            this.server_id = Integer.parseInt(json.get("server_id").toString());
            this.server_name = json.get("server_name").toString();
            this.source = json.get("source").toString();
            this.rol_id = json.get("rol_id").toString();

            JSONArray ja = (JSONArray) json.get("commands");
            for (Object a: ja) {
                this.cmds.add(a.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
