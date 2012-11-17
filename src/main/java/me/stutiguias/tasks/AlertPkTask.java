/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.tasks;

import java.util.Iterator;
import java.util.Map;
import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.mcpk.PK;
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
            
            for(Iterator it = plugin.IsPk.entrySet().iterator();it.hasNext();)
            {         
                // key=value separator this by Map.Entry to get key and value
                Map.Entry m =(Map.Entry)it.next();
              
                // getKey is used to get key of Map
                String key=(String)m.getKey();

                // getValue is used to get value of key in Map
                PK value=(PK)m.getValue();
                
                if(plugin.getServer().getOnlinePlayers().length > 0 && value.getKills() >= plugin.turnpk)
                {
                    Player pkPlayer =  plugin.getServer().getPlayer(key);
                    if(pkPlayer != null) {
                        Double PkPlayerX = pkPlayer.getLocation().getX();
                        Double PkPlayerZ = pkPlayer.getLocation().getZ();
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

                if(plugin.getCurrentMilli() > plugin.IsPk.get(key).getTime()) {
                    Player _PKiller = plugin.getServer().getPlayer(key);
                    String[] playersgroups = plugin.IsPk.get(key).getPkOldGroups();
                    for (int i = 0; i < playersgroups.length; i++) {
                         plugin.permission.playerAddGroup(_PKiller, playersgroups[i]);
                    }
                    plugin.permission.playerRemoveGroup(_PKiller, plugin.GroupPk);
                    plugin.IsPk.remove(key);
                }

            }
      } catch (Exception ex) {
         ex.printStackTrace();
      }        
        
        
    }
    
    private String parseColor(String message) {
	 for (ChatColor color : ChatColor.values()) {
            message = message.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }
        return message;
    }
}
