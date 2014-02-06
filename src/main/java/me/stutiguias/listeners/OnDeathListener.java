/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import java.util.Map;
import me.stutiguias.mcpk.Bonus;
import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.model.MCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author Daniel
 */
public class OnDeathListener implements Listener {
    
    private final Mcpk plugin;
    private final Bonus _Bonus;
    
    public OnDeathListener(Mcpk plugin){
        this.plugin = plugin;
        _Bonus = new Bonus(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void PlayerDeath(PlayerDeathEvent event) {
        if(isInvalid(event)) return;

        Player killer = event.getEntity().getKiller();
        MCPlayer mcVictim;
        MCPlayer mcKiller;

        try{
            String killerName = event.getEntity().getKiller().getName();
            String victim = event.getEntity().getName();
            mcKiller = plugin.MCPlayers.get(killerName); 
            mcVictim = plugin.MCPlayers.get(victim); 
        }catch(NullPointerException ex) {
            return;
        }
        
        mcKiller.addKills(1);
        
        int kills = plugin.DB.getPlayer(killer).getKills();
        plugin.DB.UpdateKill(killer, kills + 1);
        
        
        if(!mcVictim.IsPK() && mcKiller.getKills() >= plugin.turnpk) {
            mcKiller.setIsPK(Boolean.TRUE);
        }
        
        if(mcKiller.IsPK()){
            
            if(mcKiller.getPKMsg()) killer.sendMessage("You have total of " + plugin.DB.getPlayer(killer).getKills() + " kill(s)");
            
            mcKiller.setPKTime(plugin.GetCurrentMilli() + plugin.time);
            _Bonus.getBonusForPK(mcKiller.getName(),killer);
       
            if(plugin.ChangePkGroup) {
                ChangeGroup(killer, mcKiller);
            }
            
            BroadcastDeathMessage(mcKiller);
            
        }else if (mcVictim.IsPK()){
            mcKiller.setKillPk(Boolean.TRUE);
        }
        
    }
    
    private boolean isInvalid(PlayerDeathEvent event) {
        if(!(event.getEntity().getKiller() instanceof Player)) {
            return true;
        }
        return !(event.getEntity() instanceof Player);
    }
    
    private void ChangeGroup(Player killer,MCPlayer mcKiller){
        String[] playersgroups = plugin.permission.getPlayerGroups(killer);
        if(plugin.RemoveAllOtherGroup) {
            for (String playersgroup : playersgroups) {
                plugin.permission.playerRemoveGroup(killer, playersgroup);
            }
        }
        mcKiller.setPkOldGroups(playersgroups);
        plugin.permission.playerAddGroup(killer, plugin.GroupPk);
    }
    
    private void BroadcastDeathMessage(MCPlayer mcKiller) {
        for(Map.Entry<Integer,String> announcekills : plugin.translate.PkMsg.entrySet())
        {
            if(mcKiller.getKills() != announcekills.getKey()) continue; 
            plugin.getServer().broadcastMessage(plugin.parseColor(announcekills.getValue().replace("%player%", mcKiller.getName() )));
        }
    }
}
