/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.tasks;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Stutiguias
 */
public class AlertPkTask implements Runnable {
   
    private final Mcpk plugin;

    public AlertPkTask(Mcpk plugin) {
            this.plugin = plugin;
    }
    
    @Override
    public void run() {
      try{
            Player[] playerList = plugin.getServer().getOnlinePlayers();
            for(String key : plugin.IsPk.keySet())
            {         
                if(plugin.getServer().getOnlinePlayers().length > 0)
                {
                    Player pkPlayer =  plugin.getServer().getPlayer(key);
                    if(pkPlayer != null) {
                        Double PkPlayerX = plugin.getServer().getPlayer(key).getLocation().getX();
                        Double PkPlayerZ = plugin.getServer().getPlayer(key).getLocation().getZ();
                        for (Player player : playerList) {
                                Double playerX = player.getLocation().getX();
                                Double playerZ = player.getLocation().getZ();
                                if ((playerX < PkPlayerX + (double)plugin.radius) &&
                                    (playerX > PkPlayerX - (double)plugin.radius) &&
                                    (playerZ < PkPlayerZ + (double)plugin.radius) &&
                                    (playerZ > PkPlayerZ - (double)plugin.radius) &&
                                    (player.getName().equals(key) == false) 
                                ) {
                                    player.sendMessage(parseColor(plugin.msg.replace("%player%", key)));
                                }
                        }
                    }
                }

                if(plugin.getCurrentMilli() > plugin.IsPk.get(key).getTime())
                    plugin.IsPk.remove(key);

            }
      } catch (Exception ex) {
         
      }        
        
        
    }
    
    private String parseColor(String message) {
	 for (ChatColor color : ChatColor.values()) {
            message = message.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }
        return message;
    }
}
