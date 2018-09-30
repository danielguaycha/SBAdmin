package com.detzerg.sbadmin.Modules.Util;

import com.detzerg.sbadmin.BungeePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BUtil {

    private static String prefix = "&lD&3&lzr &8: &7";

    public static BaseComponent[]message(String text){
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', text)).create();
    }

    public static BaseComponent[]f(String text){
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', prefix+text)).create();
    }

    public static void say(String msg){
        BungeePlugin.getMain().getProxy().getConsole().sendMessage(f(msg));
    }

    public static String f(){
        return "";
    }

}
