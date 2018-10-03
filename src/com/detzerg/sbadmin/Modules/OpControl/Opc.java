package com.detzerg.sbadmin.Modules.OpControl;

import com.detzerg.sbadmin.Commands.Spigot.SOpctrlCmd;
import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.OfflinePlayer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Opc {

    private final Map<UUID, OfflinePlayer> operators = new LinkedHashMap<>();
    private String hash;
    private SpigotPlugin main;
    public Opc(){
        main = SpigotPlugin.getMain();
        main.getCommand("opc").setExecutor(new SOpctrlCmd());
    }

    public void setHash(String input, boolean isHash){

        if (isHash) {
            this.hash = input;
            return;
        }

        String pass = input + " :^) Enjoy!";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            StringBuilder hashed = new StringBuilder();
            for (byte b : hash) {
                hashed.append(String.format("%02X", b));
            }
            this.hash = hashed.toString().toLowerCase();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getHash(){
        return this.hash;
    }

}
