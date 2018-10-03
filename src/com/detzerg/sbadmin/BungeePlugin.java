package com.detzerg.sbadmin;

import com.detzerg.sbadmin.Commands.Bungee.*;
import com.detzerg.sbadmin.Modules.Config.BungeeConfig;
import com.detzerg.sbadmin.Modules.Db;
import com.detzerg.sbadmin.Modules.Mqtt;
import com.detzerg.sbadmin.Modules.Redis.RedisPlayer;
import com.detzerg.sbadmin.Modules.Util.BUtil;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Set;


public class BungeePlugin  extends Plugin{
    private static BungeePlugin main;;
    private BungeeConfig c;
    private static Db db;
    private Mqtt mq;
    private HashMap<String, RedisPlayer> reports;
    @Override
    public void onEnable() {
        setMain(this);
        getProxy().getPluginManager().registerCommand(this, new BAdminCmd(this));
        load();
        getLogger().info("Plugin Ecregister activado con exito...");
    }

    @Override
    public void onDisable() {
        if (db!=null) {
            db.closeConnection();
            db = null;
        }
        if (mq!=null) {
            mq.close();
            mq = null;
        }
        c = null;
    }

    public void load(){
        c = new BungeeConfig();

        if (c.useDb())
            db = new Db(this);

        if (c.isValid()) {
            mq = new Mqtt(c);
            BUtil.say("&aPlugin BungeeAdmin 100% operational");
            getProxy().getPluginManager().registerCommand(this, new BSyncCmd(this));
            getProxy().getPluginManager().registerCommand(this, new BReportCmd(this));
        }
        else {
            BUtil.say("Ingrese un server_id y server_name unico, luego ejecute &c/badmin &freload");
        }
        getProxy().getPluginManager().registerCommand(this, new BRegisterCmd(this));
        getProxy().getPluginManager().registerCommand(this, new BGotoCmd(this));

        if (c.redisMode()) {
            getProxy().getPluginManager().registerCommand(this, new BFindCmd(this));
            this.reports = new HashMap<>();
        }
    }

    private static void setMain(BungeePlugin main){
        BungeePlugin.main = main;
    }
    public static BungeePlugin getMain(){
        return main;
    }
    public BungeeConfig getC() {
        return c;
    }
    public static Db getDb(){
        return db;
    }
    public Mqtt getMqtt(){
        return this.mq;
    }
    public HashMap<String, RedisPlayer> getReports(){
        return reports;
    }
}
