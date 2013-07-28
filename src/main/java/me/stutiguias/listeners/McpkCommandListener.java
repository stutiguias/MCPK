/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.listeners;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        switch(args[0]) {
            case "reload":
                 Reload(cs);
                 return true;
            case "alertmsg":
                AlertMsg(cs);
                return true;
            case "pkmsg":
                PKMsg(cs);
                return true;
            case "removepk":
                if(!plugin.hasPermission(cs.getName(),"mcpk.command.leftpk"))
                    return false;
                RemovePkStatus(cs);
                return true;
            default:
                return false;
        }
    }
    
    public void RemovePkStatus(CommandSender cs) {
        Player player = plugin.getServer().getPlayer(cs.getName());
        plugin.MCPlayers.get(player.getName()).setKills(0);
        plugin.MCPlayers.get(player.getName()).setIsPK(Boolean.FALSE);
        player.sendMessage(plugin.parseColor("You remove the mcpk status"));
    }
    
    public void Reload(CommandSender cs) {
        cs.sendMessage("Reloading!");
        plugin.OnReload();
        cs.sendMessage("Reload Done!");    
    }
    
    public void AlertMsg(CommandSender cs) {
        if(cs instanceof Player) {
            Player _Player = (Player)cs;
            if(plugin.DB.getAlertMsg(_Player)) {
                plugin.DB.UpdateAlertMsg(_Player, Boolean.FALSE);
                plugin.MCPlayers.get(_Player.getName()).setAlertMsg(Boolean.FALSE);
                cs.sendMessage("Now you not will receve any Alert PK MSG");
            }else{
                plugin.DB.UpdateAlertMsg(_Player, Boolean.TRUE);
                plugin.MCPlayers.get(_Player.getName()).setAlertMsg(Boolean.TRUE);
                cs.sendMessage("Now you will receve any Alert PK MSG");
            }
        }
    }
    
    public void PKMsg(CommandSender cs) {
        if(cs instanceof Player) {
            Player _Player = (Player)cs;
            if(plugin.DB.getPKMsg(_Player)) {
                plugin.DB.UpdatePKMsg(_Player, Boolean.FALSE);
                plugin.MCPlayers.get(_Player.getName()).setPKMsg(Boolean.FALSE);
                cs.sendMessage("Now you not will receve any PK MSG");
            }else{
                plugin.DB.UpdatePKMsg(_Player, Boolean.TRUE);
                plugin.MCPlayers.get(_Player.getName()).setPKMsg(Boolean.TRUE);
                cs.sendMessage("Now you will receve any PK MSG");
            }
        }
    }
}
