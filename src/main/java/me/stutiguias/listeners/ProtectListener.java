/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import java.sql.Date;
import java.util.logging.Level;
import me.stutiguias.model.MCPlayer;
import me.stutiguias.mcpk.Mcpk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author Daniel
 */
public class ProtectListener implements Listener {
     
    private final Mcpk plugin;
    
    public ProtectListener(Mcpk plugin){
        this.plugin = plugin;
    }
    
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event){
      if(!plugin.usenewbieprotect) {
            return;
      }
      
      if(event instanceof EntityDamageByEntityEvent) {
        Entity attacker;
        Entity defender;
        
        try {
            EntityDamageByEntityEvent EDE = (EntityDamageByEntityEvent)event;
            if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
            {
                attacker = (Entity) ((Projectile) EDE.getDamager()).getShooter();
            }else {
                attacker = EDE.getDamager();
            }
            defender = EDE.getEntity();
            if(attacker == null || defender == null) {
                return;
            }
            if(attacker instanceof Player && defender instanceof Player) {
                Player df = (Player)defender;
                Player at = (Player)attacker;
                if(plugin.MCPlayers.get(df.getName()).getProtectAlreadyLeft()) {
                    return;
                }
                MCPlayer dfPkPlayer = plugin.MCPlayers.get(df.getName());
                MCPlayer atPkPlayer = plugin.MCPlayers.get(at.getName());
                Date dt = plugin.util.now();
                if(dfPkPlayer == null || atPkPlayer == null) {
                    return;
                }
                if(dt.before(dfPkPlayer.getNewBieProtectUntil()) ||
                   dt.before(atPkPlayer.getNewBieProtectUntil())) {
                    at.sendMessage("This player is protect until " + dfPkPlayer.getNewBieProtectUntil().toString());
                    df.sendMessage("This player is protect until " + dfPkPlayer.getNewBieProtectUntil().toString());
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
        }catch(Exception e) {
            Mcpk.logger.log(Level.WARNING, "[MCPK] {0}", e.getMessage());
        }

         
      }
    }
}
