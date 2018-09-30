package com.detzerg.sbadmin.Modules.Reporter;

import com.detzerg.sbadmin.Modules.Util.SUtil;
import com.detzerg.sbadmin.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class Report implements Listener{
    private SpigotPlugin main;
    private ReportConfig rc;
    private HashMap<Player, String> reports;

    public Report(SpigotPlugin main){
        this.main = main;
        this.rc = new ReportConfig(main);
        this.reports = new HashMap<>();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    public void menu(Player p, String victim){

        //this.reports.remove(p);
        this.reports.put(p, victim);
        System.out.println(reports.size());

        Inventory menu = Bukkit.createInventory(null, rc.cfg().getInt("MainSlots"), SUtil.t(rc.cfg().getString("MainMenuTitle")));

        ItemStack bug = SUtil.getItem(Material.getMaterial(rc.cfg().getString("BUG.item")), 1, 0,
                rc.cfg().getString("BUG.name"), rc.cfg().getList("BUG.lore"));

        ItemStack hack = SUtil.getItem(Material.getMaterial(rc.cfg().getString("HACK.item")), 1, 0,
                rc.cfg().getString("HACK.name"), rc.cfg().getList("HACK.lore"));

        ItemStack backsword = SUtil.getItem(Material.getMaterial(rc.cfg().getString("BAD-WORDS.item")), 1, 0,
                rc.cfg().getString("BAD-WORDS.name"), rc.cfg().getList("BAD-WORDS.lore"));

        ItemStack spam = SUtil.getItem(Material.getMaterial(rc.cfg().getString("SPAM.item")), 1, 0,
                rc.cfg().getString("SPAM.name"), rc.cfg().getList("SPAM.lore"));


        ItemStack team = SUtil.getItem(Material.getMaterial(rc.cfg().getString("TEAM.item")), 1, 0,
                rc.cfg().getString("TEAM.name"), rc.cfg().getList("TEAM.lore"));

        ItemStack camper = SUtil.getItem(Material.getMaterial(rc.cfg().getString("CAMPER.item")), 1, 0,
                rc.cfg().getString("CAMPER.name"), rc.cfg().getList("CAMPER.lore"));


        if (rc.cfg().getBoolean("BUG.enable"))
           menu.setItem(rc.cfg().getInt("BUG.slot"), bug);

        if (rc.cfg().getBoolean("HACK.enable"))
            menu.setItem(rc.cfg().getInt("HACK.slot"), hack);

        if (rc.cfg().getBoolean("BAD-WORDS.enable"))
            menu.setItem(rc.cfg().getInt("BAD-WORDS.slot"), backsword);

        if (rc.cfg().getBoolean("SPAM.enable"))
            menu.setItem(rc.cfg().getInt("SPAM.slot"), spam);

        if (rc.cfg().getBoolean("TEAM.enable"))
            menu.setItem(rc.cfg().getInt("TEAM.slot"), team);

        if (rc.cfg().getBoolean("CAMPER.enable"))
            menu.setItem(rc.cfg().getInt("CAMPER.slot"), camper);

        p.openInventory(menu);
    }

    public void menuHacks(Player p) {
        Inventory menu = Bukkit.createInventory(null, rc.cfg().getInt("HackMenuSlots"),
                SUtil.t(rc.cfg().getString("HackMenuTitle")));

        menu.setContents(rc.getItems());

        ItemStack back = SUtil.getItem(Material.getMaterial(rc.cfg().getString("BACK.item")), 1, 0,
                rc.cfg().getString("BACK.name"), rc.cfg().getList("BACK.lore"));

        if (rc.cfg().getBoolean("BACK.enable")) {
            menu.setItem(rc.cfg().getInt("BACK.slot"), back);
        }

        p.openInventory(menu);
    }

    @EventHandler
    public void click(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Inventory inv = e.getInventory();

        if (item == null)
            return;

        if (item.getType() == Material.AIR)
            return;

        if (inv.getName().equals(SUtil.t(rc.cfg().getString("MainMenuTitle")))){
            if (item.getType() == Material.getMaterial(rc.cfg().getString("HACK.item"))){
                menuHacks(p);
            }else{
                publish(p, reports.get(p), (item.getItemMeta().getDisplayName()));
                p.getOpenInventory().close();
                reports.remove(p);
            }
            e.setCancelled(true);
        }

        if (inv.getName().equals(SUtil.t(rc.cfg().getString("HackMenuTitle")))){
            if (item.getType() == Material.getMaterial(rc.cfg().getString("BACK.item"))){
                p.getOpenInventory().close();
                menu(p, reports.get(p));
            }else{
                publish(p, reports.get(p), item.getItemMeta().getDisplayName());
                p.getOpenInventory().close();
                reports.remove(p);
            }
            e.setCancelled(true);
        }
    }

    private void publish(Player p, String victim, String reason){
        if (p == null || victim == null || victim.equals("") || reason == null || reason.equals("")){
            return;
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                JSONArray cmds = new JSONArray();
                cmds.add("breport "+p.getDisplayName()+" "+victim+" "+ ChatColor.stripColor(reason));
                JSONObject json = new JSONObject();
                json.put("player", p.getDisplayName());
                json.put("player_id", 1);
                json.put("server_id", -1);
                json.put("server_name", "bungees");
                json.put("rol_id", "Member");
                json.put("source", "spigotConsole");
                json.put("commands", cmds);

                if (main.getMqtt()!=null) {
                    main.getMqtt().publish(json.toJSONString(), main.topic);
                }
                else
                   SUtil.say("Aun no se ha configurado el 'id' y 'name' para este server!");
            }
        }.runTaskAsynchronously(main);
    }

}
