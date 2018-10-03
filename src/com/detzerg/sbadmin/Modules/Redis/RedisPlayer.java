package com.detzerg.sbadmin.Modules.Redis;

public class RedisPlayer {
    private String origin;
    private String victim;
    private String victim_server;
    private String WhoFind;

    public RedisPlayer() {
    }

    public RedisPlayer(String origin, String victim, String victim_server, String whoFind) {
        this.origin = origin;
        this.victim = victim;
        this.victim_server = victim_server;
        WhoFind = whoFind;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getVictim() {
        return victim;
    }

    public void setVictim(String victim) {
        this.victim = victim;
    }

    public String getVictim_server() {
        return victim_server;
    }

    public void setVictim_server(String victim_server) {
        this.victim_server = victim_server;
    }

    public String getWhoFind() {
        return WhoFind;
    }

    public void setWhoFind(String whoFind) {
        WhoFind = whoFind;
    }
}
