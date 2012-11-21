
package me.stutiguias.listeners;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import me.stutiguias.mcpk.Bonus;
import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.mcpk.MCPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Stutiguias
 */
public class McpkPlayerListener implements Listener {
    
    private final Mcpk plugin;
    private Bonus _Bonus;
    
    public McpkPlayerListener(Mcpk plugin){
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
            
            plugin.MCPlayers.get(killer).setTime(plugin.getCurrentMilli() + plugin.time);
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
            if(event.getCause() == DamageCause.PROJECTILE)
            {
                attacker = ((Projectile) EDE.getDamager()).getShooter();
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
                MCPlayer dfPkPlayer = plugin.DataBase.getPlayer(df.getName());
                MCPlayer atPkPlayer = plugin.DataBase.getPlayer(at.getName());
                Date dt = now();
                if(dfPkPlayer == null || atPkPlayer == null) {
                    return;
                }
                if(dt.before(dfPkPlayer.getNewBie()) || dt.before(atPkPlayer.getNewBie())) {
                    at.sendMessage("This player is protect until " + dfPkPlayer.getNewBie().toString());
                    df.sendMessage("This player is protect until " + dfPkPlayer.getNewBie().toString());
                    event.setCancelled(true);
                    event.setDamage(0);
                }
            }
        }catch(Exception e) {
            Mcpk.log.log(Level.WARNING, "[MCPK] {0}", e.getMessage());
        }

         
      }
    }
    
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.MCPlayers.remove(event.getPlayer().getName());
    }
    
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player pl = event.getPlayer();
        MCPlayer _MCPlayer = null;
        try {
            _MCPlayer = plugin.DataBase.getPlayer(pl.getName());
        }catch(Exception e){
            Mcpk.log.log(Level.WARNING, "[MCPK] Error get Player from Database: {0}", e.getMessage());
        }
        if(_MCPlayer == null) {
            Date dt = now();
            if(!plugin.usenewbieprotect) {
                plugin.DataBase.createPlayer(pl.getName(), "0", 0,new Timestamp(dt.getTime())); 
                Mcpk.log.log(Level.INFO, "[MCPK] New Player Found {0}", pl.getName());
            }else{
                dt = addTime(plugin.NewbieProtectTime);
                Timestamp ProtectUntil = new Timestamp(dt.getTime());
                plugin.DataBase.createPlayer(pl.getName(), "0", 0,ProtectUntil); 
                pl.sendMessage(plugin.protecmsg.replace("%d%",String.valueOf(plugin.NewbieProtectTime)).replace("%date%",ProtectUntil.toString()));
                Mcpk.log.log(Level.INFO, "[MCPK] New Player Found {0} is protected until {1}", new Object[]{pl.getName(),ProtectUntil.toString()});
            }
            _MCPlayer = new MCPlayer(pl.getName(),dt);
            plugin.MCPlayers.put(pl.getName(),_MCPlayer);
        }else {
            _MCPlayer.setKills(0);
            _MCPlayer.setAlertMsg(Boolean.TRUE);
            _MCPlayer.setPKMsg(Boolean.TRUE);
            _MCPlayer.setIsPK(Boolean.FALSE);
            plugin.MCPlayers.put(pl.getName(), _MCPlayer);
        }
        
    }
    
    public Date addTime(String Time)
    {
        Calendar cal = Calendar.getInstance();
        if(Time.contains("m")) {
            cal.add(Calendar.MINUTE, Integer.parseInt(Time.replace("m","")) );    
        }
        java.sql.Date dataSql = new java.sql.Date(cal.getTime().getTime()); 
        return dataSql;
    }
    
    public Date now() {
        java.util.Date dataUtil = new java.util.Date();  
        java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());  
        return dataSql;
    }
}
