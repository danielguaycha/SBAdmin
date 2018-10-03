package com.detzerg.sbadmin;

import com.detzerg.sbadmin.Commands.Spigot.SReloadCmd;
import com.detzerg.sbadmin.Modules.Config.SpigotConfig;
import com.detzerg.sbadmin.Modules.Db;
import com.detzerg.sbadmin.Modules.Mqtt;
import com.detzerg.sbadmin.Modules.OpControl.Opc;
import com.detzerg.sbadmin.Modules.Reporter.Report;
import com.detzerg.sbadmin.Modules.Util.SUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlugin extends JavaPlugin {
    private static SpigotPlugin main;
    private SpigotConfig c;
    private static Db db;
    private Mqtt mqtt;
    private Report rp;
    private Opc opc;
    @Override
    public void onEnable() {
        main = this;
        getCommand("sadmin").setExecutor(new SReloadCmd(this));
        load();
    }


    @Override
    public void onDisable() {
        if (this.rp!=null)
            this.rp = null;

        if (this.mqtt!=null) {
            this.getMqtt().close();
            this.mqtt = null;
        }

        if (db!=null) {
            db.closeConnection();
            db = null;
        }

        if (c!=null)
            c = null;
    }

    public void load(){
        c = new SpigotConfig(this);

        if (c.useDb())
            db = new Db(this);

        if (c.isValid()) {
            mqtt = new Mqtt(c);
            rp = new Report(this);
            SUtil.say("&aPlugin SpigotAdmin 100% operational!");
        }
        else{
            SUtil.say("&fIngrese un server_id y server_name unico, luego ejecute &c/sadmin &freload");
        }
        //opc = new Opc();
    }

    public SpigotConfig getCfg(){
        return this.c;
    }
    public static Db getDb(){
        return db;
    }
    public static SpigotPlugin getMain(){
        return main;
    }
    public Report getRp(){
        return this.rp;
    }
    public Mqtt getMqtt(){
        return this.mqtt;
    }
    public Opc getOpc(){
        return this.opc;
    }
}
