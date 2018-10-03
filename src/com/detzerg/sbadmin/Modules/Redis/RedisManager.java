package com.detzerg.sbadmin.Modules.Redis;


import com.detzerg.sbadmin.BungeePlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RedisManager {

    public RedisManager(String jsonString){
        if (jsonString.length()>0) {
            JSONParser jp = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) jp.parse(jsonString);
                RedisPlayer rp = new RedisPlayer();
                rp.setOrigin(json.get("Origin").toString());
                rp.setVictim(json.get("Victim").toString());
                rp.setVictim_server(json.get("Victim_Server").toString());
                rp.setWhoFind(json.get("WhoFind").toString());
                BungeePlugin.getMain().getReports().put(rp.getWhoFind(), rp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
