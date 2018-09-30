package com.detzerg.sbadmin.Modules.Util;

import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class SUtil {

    private static String prefix = "&lD&3&lzr &8: &7";

    public static ItemStack getItem(Material material, int amount, int n, String name, List<?> lore) {
        ItemStack localItem;

        if(n>0)
            localItem = new ItemStack(material, amount, (short)n);
        else
            localItem = new ItemStack(material);

        if(!name.equals("")){
            ItemMeta meta =localItem.getItemMeta();
            meta.setDisplayName(message(name));
            if (lore !=null && lore.size()>0) {
                meta.setLore(message(lore));
            }
            localItem.setItemMeta(meta);
        }

        return localItem;
    }

    public static String message(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String f(String msg){
        return ChatColor.translateAlternateColorCodes('&', prefix+msg);
    }

    public static String t(String title){
        return title.replace("&", "§");
    }

    public static List<String> message(List<?> list){
        List<String>localList = new ArrayList<>();
        list.forEach(data -> {
            localList.add(ChatColor.translateAlternateColorCodes('&', data.toString()));
        });
        return localList;
    }

    public static boolean hp(CommandSender sender, String s) {
        if (sender.hasPermission("bs." + s)) {
            return true;
        }
        return false;
    }

    public static void say(String msg){
        SpigotPlugin.getMain().getServer().getConsoleSender().sendMessage(message(prefix+msg));
    }
}
