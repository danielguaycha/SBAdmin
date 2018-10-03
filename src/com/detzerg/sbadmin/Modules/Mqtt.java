package com.detzerg.sbadmin.Modules;
import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Config.BungeeConfig;
import com.detzerg.sbadmin.Modules.Config.SpigotConfig;
import com.detzerg.sbadmin.Modules.Executor.ExecutorManager;
import com.detzerg.sbadmin.Modules.Redis.RedisManager;
import com.detzerg.sbadmin.SpigotPlugin;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Mqtt implements MqttCallback{
    private int reconect;
    private Db db;
    private MqttClient client;
    private String execTopic;

    private int server_id;
    private String server_name;
    private String server_description;
    private String server_type;

    private String mqtt_server;
    private String user;
    private String password;
    private boolean mqtt_redis;

    public Mqtt(BungeeConfig cfg){
        this.db = BungeePlugin.getDb();
        this.execTopic = Env.topicExec;
        this.reconect = 0;

        this.server_id = cfg.getConfig().getInt("server_id");
        this.server_name =cfg.getConfig().getString("server_name");
        this.server_description =cfg.getConfig().getString("server_description");
        this.server_type = "Bungee";

        this.mqtt_server = cfg.getConfig().getString("mqtt_server")+":"+cfg.getConfig().getInt("mqtt_port");
        this.user = cfg.getConfig().getString("mqtt_user");
        this.password = cfg.getConfig().getString("mqtt_password");
        this.mqtt_redis = cfg.redisMode();

        connect();

        if (cfg.useDb())
            installServer();

    }

    public Mqtt(SpigotConfig cfg) {
        this.db = SpigotPlugin.getDb();
        this.execTopic = Env.topicExec;
        this.reconect = 0;

        this.server_id = cfg.getConfig().getInt("server_id");
        this.server_name =cfg.getConfig().getString("server_name");
        this.server_description =cfg.getConfig().getString("server_description");
        this.server_type = "Spigot";

        this.mqtt_server = cfg.getConfig().getString("mqtt_server")+":"+cfg.getConfig().getInt("mqtt_port");
        this.user = cfg.getConfig().getString("mqtt_user");
        this.password = cfg.getConfig().getString("mqtt_password");
        this.mqtt_redis = false;
        connect();
        if(cfg.useDb())
            installServer();
    }

    private void installServer(){
        db.conect();
        if(existServer()){
            db.closeConnection();
            return;
        }

        java.util.Date date = new Date();

        String sql ="INSERT INTO `servers` (`server_uuid`, `server_name`, " +
                "`server_description`, `server_type`, `created_at`, `updated_at`) " +
                "VALUES (?,?,?,?,Now(), Now());";

        try {
            PreparedStatement pst = db.getConnection().prepareStatement(sql);
            pst.setInt(1, this.server_id);
            pst.setString(2, this.server_name);
            pst.setString(3, this.server_description);
            pst.setString(4, this.server_type);

            pst.executeUpdate();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean existServer(){
        ResultSet rs = db.select("SELECT id FROM `servers` " +
                "WHERE `server_uuid` = "+this.server_id+" " +
                "AND `server_name`= '"+this.server_name+"' ");
        try {
            if(rs.next()){
                System.out.println("[DetzCode] Server synchronized previously");
                rs.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void connect(){
        try{
            client = new MqttClient(this.mqtt_server,
                    this.server_name+"_"+this.server_id, new MemoryPersistence());

            MqttConnectOptions conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(true);
            conOpt.setUserName(this.user);
            conOpt.setPassword(this.password.toCharArray());

            client.setCallback(this);
            client.connect(conOpt);
            client.subscribe(execTopic);

            if (this.mqtt_redis){
                client.subscribe(Env.topicRedis);
            }
            reconect = 0;
        }
        catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String msg, String topic){
        if (!this.isConnected()){
            System.out.println("Por alguna razon el cliente se desconecto, intente nuevamente");
            return;
        }
        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            client.publish(topic, message);
        }catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if (client.isConnected() && client!=null) {
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        if (client.isConnected()) {
            reconect = 0;
            return true;
        }
        else {
            try {
                client.reconnect();
            } catch (MqttException e) {
                System.out.println("Reconexion Fallida!");
            }
            return isConnected();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
      if (!isConnected() && reconect<5){
          reconect++;
      }
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        if (!Env.production)
            System.out.println(String.format("[%s] %s", s, new String(mqttMessage.getPayload())));

        if(s.equals(this.execTopic)) {
            new ExecutorManager(this.server_type, new String(mqttMessage.getPayload()));
        }

        if (s.equals(Env.topicRedis)){
            new RedisManager(new String(mqttMessage.getPayload()));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}

