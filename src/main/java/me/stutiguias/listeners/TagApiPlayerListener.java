/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

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
    public void onNameTag(PlayerReceiveNameTagEvent event) {
            if(plugin.IsPk.get(event.getNamedPlayer().getName()) != null) {
                event.setTag( ChatColor.RED + event.getNamedPlayer().getName() );
            }else{
                event.setTag( event.getNamedPlayer().getName() );
            }
    }
    
}
