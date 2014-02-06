/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

/**
 *
 * @author Daniel
 */
public class TagApiPlayerListener implements Listener {
    
    Mcpk plugin;
    
    public TagApiPlayerListener(Mcpk instance) {
        plugin = instance;
    }
    
    @EventHandler
    public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
        String Name = event.getNamedPlayer().getName();
        if(plugin.MCPlayers.get(Name).IsPK()) {
            event.setTag( ChatColor.RED + Name );
        }else if(plugin.TurnGreenAfterKillPk && plugin.MCPlayers.get(Name).getKillPk() && !plugin.MCPlayers.get(Name).IsPK()){
            event.setTag( ChatColor.GREEN + Name );
        }else{
            event.setTag( Name );
        }
    }
    
}
