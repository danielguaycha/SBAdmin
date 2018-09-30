package com.detzerg.sbadmin.Commands.Bungee;

import com.detzerg.sbadmin.BungeePlugin;
import com.detzerg.sbadmin.Modules.Http;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Map;

public class BRegisterCmd extends Command {
    private BungeePlugin b;
    private String wm = ChatColor.translateAlternateColorCodes('&', "&6WM&7: ");
    public BRegisterCmd(BungeePlugin b) {
        super("ecweb");
        this.b = b;
    }
    @Override
    public void execute(CommandSender sender, String[] arg) {
        String helpPrefix = ChatColor.DARK_GRAY + "- " + ChatColor.DARK_AQUA;
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(arg.length==2){
                //reset password
                if(arg[0].equalsIgnoreCase("reset")){
                    if(arg[1].length()<6){
                        player.sendMessage(message(ChatColor.RED+"La contraseña debe ser mayor a 5 caracteres"));
                        return;
                    }
                    this.resetPassword(player, arg[1]);
                    return;
                }
                // register
                if(!arg[0].contains("@") && (arg[0].contains(".es") || !arg[0].contains(".com") || !arg[0].contains(".net"))){
                    player.sendMessage(message(ChatColor.RED+"Debes ingresar un email valido"));
                    return;
                }
                if(arg[1].length()<6){
                    player.sendMessage(message(ChatColor.RED+"La contraseña debe ser mayor a 5 caracteres"));
                    return;
                }
                b.getProxy().getScheduler().runAsync(b, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> data = new HashMap<>();
                            data.put("name", player.getDisplayName());
                            data.put("nick", player.getDisplayName());
                            data.put("email", arg[0]);
                            data.put("password", arg[1]);
                            player.sendMessage(message(wm+ChatColor.AQUA+"Validando datos, espere porfavor..."));
                            String response = Http.post(BungeePlugin.getMain().getC().getConfig().getString("url_register"), data);
                            player.sendMessage(message(wm+ChatColor.GREEN+Http.getResponse(response, 1)));
                            player.sendMessage(message(wm+ChatColor.AQUA+"Registro finalizado!"));
                        } catch (Exception e) {
                            player.sendMessage(message(wm+ChatColor.RED+"Hay un error al registrar, porfavor reportalo en el foro!"));
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }
            else{
                player.sendMessage(message("&e-&c======&9--- &l&7Ecuacraft Web &9---&c======&e-"));
                player.sendMessage(message(helpPrefix + "/ecweb" + ChatColor.YELLOW + " <email> <password> "+ChatColor.GRAY+":: Registrate en la web"));
                player.sendMessage(message(helpPrefix+"/ecweb reset"+ChatColor.YELLOW+" <password> "+ChatColor.GRAY+":: Cambia tu contraseña de la web"));
            }
        }else{
            sender.sendMessage(message("&aEstos comandos solo pueden ser ejecutados por jugadores!"));
        }
    }

    private void resetPassword(ProxiedPlayer p, String password){
        b.getProxy().getScheduler().runAsync(b, new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> data = new HashMap<>();
                    data.put("nick", p.getDisplayName());
                    data.put("password", password);

                    p.sendMessage(message(wm+ChatColor.AQUA+"Validando datos, espere porfavor. No se desconecte"));
                    String response = Http.post(BungeePlugin.getMain().getC().getConfig().getString("url_restore"), data);
                    p.sendMessage(message(wm+ChatColor.GREEN+Http.getResponse(response, 1)));
                    p.sendMessage(message(wm+ChatColor.AQUA+"Actualizacion finalizada!"));

                } catch (Exception e) {
                    p.sendMessage(message(ChatColor.RED+"Hay un error al registrar, porfavor reportalo en el foro!"));
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private BaseComponent []message(String text){
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', text)).create();
    }
}
