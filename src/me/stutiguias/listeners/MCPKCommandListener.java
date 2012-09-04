/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Daniel
 */
public class MCPKCommandListener implements CommandExecutor {
      
    public Mcpk plugin;
    
    public MCPKCommandListener(Mcpk instance)
    {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if(args[0].equalsIgnoreCase("reload")) {
            cs.sendMessage("Reloading!");
            plugin.OnReload();
            cs.sendMessage("Reload Done!");
            return true;
        }
        return false;
    }
}
