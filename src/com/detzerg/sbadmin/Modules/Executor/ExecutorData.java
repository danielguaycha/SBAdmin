package com.detzerg.sbadmin.Modules.Executor;

import java.util.ArrayList;

public class ExecutorData {

    private int player_id;
    private String player;
    private int server_id;
    private String server_name;
    private String rol_id;
    private String source;
    private ArrayList<String> commands;

    public ExecutorData(int player_id, String player, int server_id, String server_name, String rol_id, String source, ArrayList<String> commands) {
        this.player_id = player_id;
        this.player = player;
        this.server_id = server_id;
        this.server_name = server_name;
        this.rol_id = rol_id;
        this.source = source;
        this.commands = commands;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getRol_id() {
        return rol_id;
    }

    public void setRol_id(String rol_id) {
        this.rol_id = rol_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }
}
