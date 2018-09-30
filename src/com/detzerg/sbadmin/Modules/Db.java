package com.detzerg.sbadmin.Modules;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.SpigotPlugin;

import java.sql.*;

public class Db {

    private Connection con;
    private ResultSet rs;
    private Statement st;

    private String host;
    private String database;
    private String user;
    private String password;

    public Db(BungeePlugin bungeePlugin) {
        this.host = bungeePlugin.getC().getConfig().getString("host");
        this.database = bungeePlugin.getC().getConfig().getString("database");
        this.user = bungeePlugin.getC().getConfig().getString("user");
        this.password = bungeePlugin.getC().getConfig().getString("password");
    }

    public Db(SpigotPlugin spigotPlugin) {
        this.host = spigotPlugin.getCfg().getConfig().getString("host");
        this.database = spigotPlugin.getCfg().getConfig().getString("database");
        this.user = spigotPlugin.getCfg().getConfig().getString("user");
        this.password = spigotPlugin.getCfg().getConfig().getString("password");
    }

    public void conect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+database,user,password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if ((this.con == null) || (!this.con.isValid(5))) {
                conect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public ResultSet select(String sql) {
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public int update(String sql){
        int val = 0;
        Statement st;
        try {
            st = con.createStatement();
            val = st.executeUpdate(sql);
            st.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return val;
    }

    public void closeSelect(){
        try {
            st.close();
            rs.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public void closeConnection(){
        try {
            if (this.con != null) {
                this.con.close();
                this.con = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
