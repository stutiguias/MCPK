/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import java.util.Map;
import me.stutiguias.mcpk.Bonus;
import me.stutiguias.mcpk.Mcpk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author Daniel
 */
public class McpkOnDeathListener implements Listener {
    
    private final Mcpk plugin;
    private Bonus _Bonus;
    
    public McpkOnDeathListener(Mcpk plugin){
        this.plugin = plugin;
        _Bonus = new Bonus(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void PlayerDeath(PlayerDeathEvent event) {
        if(!(event.getEntity().getKiller() instanceof Player)) {
            return;
        }
        String killer = event.getEntity().getKiller().getName();
        Player _Pkiller = event.getEntity().getKiller();
        
        // Add the kill
        plugin.MCPlayers.get(killer).addKills(1); 
        int kills = plugin.DataBase.getPlayer(killer).getKills();
        plugin.DataBase.UpdateKill(killer, kills + 1);
        
        
        if(plugin.MCPlayers.get(killer).getKills() >= plugin.turnpk) {
            plugin.MCPlayers.get(killer).setIsPK(Boolean.TRUE);
        }
        
        if(plugin.MCPlayers.get(killer).getIsPK()){
            
            if(plugin.MCPlayers.get(killer).getPKMsg()) _Pkiller.sendMessage("You have total of " + plugin.DataBase.getPlayer(killer).getKills() + " kill(s)");
            
            plugin.MCPlayers.get(killer).setPKTime(plugin.getCurrentMilli() + plugin.time);
            _Bonus.getBonusForPK(killer,_Pkiller);
       
            // if turn pk and config setting is true to change group pk
            if(plugin.ChangePkGroup) {
               // change player group
               String[] playersgroups = plugin.permission.getPlayerGroups(_Pkiller);
               for (int i = 0; i < playersgroups.length; i++) {
                   plugin.permission.playerRemoveGroup(_Pkiller, playersgroups[i]);
               }
               plugin.MCPlayers.get(killer).setPkOldGroups(playersgroups);
               plugin.permission.playerAddGroup(_Pkiller, plugin.GroupPk);
            }
            
            for(Map.Entry<Integer,String> announcekills : plugin.pkmsg.entrySet())
            {
               if(plugin.MCPlayers.get(killer).getKills() == announcekills.getKey()) { 
                    plugin.getServer().broadcastMessage(plugin.parseColor(announcekills.getValue().replace("%player%", killer)));
               }
            }
        }
        
    }
    
}
