/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.tasks;

import java.util.Map;
import me.stutiguias.mcpk.MCPlayer;
import me.stutiguias.mcpk.Mcpk;
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
            if(plugin.getServer().getOnlinePlayers().length == 0) return;
            for (Map.Entry m : plugin.MCPlayers.entrySet()) {
                String key=(String)m.getKey();

                // getValue is used to get value of key in Map
                MCPlayer Killer =(MCPlayer)m.getValue();

                Player pkPlayer = plugin.getServer().getPlayer(key);
                if(pkPlayer == null || !Killer.getIsPK()) continue;
                
                WarningPlayer(pkPlayer, playerList, key);
                Integer timeleft = Integer.parseInt(String.valueOf(plugin.MCPlayers.get(key).getPKTime() - plugin.GetCurrentMilli()));
                if(timeleft > 1000 && Killer.getPKMsg()) {
                    timeleft = timeleft / 1000;
                    pkPlayer.sendMessage("Time left on PK Status " + timeleft);
                }

                if(plugin.GetCurrentMilli() > plugin.MCPlayers.get(key).getPKTime()) {
                    Player _PKiller = plugin.getServer().getPlayer(key);
                    String[] playersgroups = plugin.MCPlayers.get(key).getPkOldGroups();
                    if(playersgroups != null && plugin.RemoveAllOtherGroup) {
                        for (int i = 0; i < playersgroups.length; i++) {
                             plugin.permission.playerAddGroup(_PKiller, playersgroups[i]);
                        }
                    }
                    plugin.permission.playerRemoveGroup(_PKiller, plugin.GroupPk);
                    plugin.MCPlayers.get(key).setIsPK(Boolean.FALSE);
                    plugin.MCPlayers.get(key).setKills(0);
                }
            }
      } catch (Exception ex) {
         ex.printStackTrace();
      }        

    }
    
    public void WarningPlayer(Player pkPlayer,Player[] playerList,String key) {
        Double PkPlayerX = pkPlayer.getLocation().getX();
        Double PkPlayerZ = pkPlayer.getLocation().getZ();
        for (Player player : playerList) {
                Double playerX = player.getLocation().getX();
                Double playerZ = player.getLocation().getZ();
                if ((playerX < PkPlayerX + (double)plugin.radius) &&
                    (playerX > PkPlayerX - (double)plugin.radius) &&
                    (playerZ < PkPlayerZ + (double)plugin.radius) &&
                    (playerZ > PkPlayerZ - (double)plugin.radius) &&
                     plugin.MCPlayers.get(player.getName()).getAlertMsg() && (player.getName().equals(key) == false) 
                ) {
                    player.sendMessage(plugin.parseColor(plugin.translate.AlertMsg.replace("%player%", key)));
                }
        }
    }
    
}
