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
public class McpkCommandListener implements CommandExecutor {
      
    public Mcpk plugin;
    private CommandSender sender;
    
    public McpkCommandListener(Mcpk instance)
    {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        this.sender = sender;
        if (sender.getName().equalsIgnoreCase("CONSOLE")) return true;
        if (!(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        
        switch(args[0]) {
            case "reload":
                 return Reload(sender);
            case "alertmsg":
                return AlertMsg(player);
            case "pkmsg":
                return PKMsg(player);
            case "removepk":
                if(!plugin.hasPermission(sender.getName(),"mcpk.command.leftpk"))
                    return false;
                RemovePkStatus(player);
                return true;
            case "help":
            case "?":
            default:
                return Help();
        }
    }
    
    public void RemovePkStatus(Player player) {
        plugin.MCPlayers.get(player.getName()).setKills(0);
        plugin.MCPlayers.get(player.getName()).setIsPK(Boolean.FALSE);
        player.sendMessage(plugin.parseColor("You remove the mcpk status"));
    }
    
    public boolean Reload(CommandSender cs) {
        cs.sendMessage("Reloading!");
        plugin.OnReload();
        cs.sendMessage("Reload Done!");    
        return true;
    }
    
    public boolean AlertMsg(Player player) {
        if(plugin.DB.getAlertMsg(player)) {
            plugin.DB.UpdateAlertMsg(player, Boolean.FALSE);
            plugin.MCPlayers.get(player.getName()).setAlertMsg(Boolean.FALSE);
            player.sendMessage("Now you not will receve any Alert PK MSG");
        }else{
            plugin.DB.UpdateAlertMsg(player, Boolean.TRUE);
            plugin.MCPlayers.get(player.getName()).setAlertMsg(Boolean.TRUE);
            player.sendMessage("Now you will receve any Alert PK MSG");
        }
        return true;
    }
    
    public boolean PKMsg(Player player) {
        if(plugin.DB.getPKMsg(player)) {
            plugin.DB.UpdatePKMsg(player, Boolean.FALSE);
            plugin.MCPlayers.get(player.getName()).setPKMsg(Boolean.FALSE);
            player.sendMessage("Now you not will receve any PK MSG");
        }else{
            plugin.DB.UpdatePKMsg(player, Boolean.TRUE);
            plugin.MCPlayers.get(player.getName()).setPKMsg(Boolean.TRUE);
            player.sendMessage("Now you will receve any PK MSG");
        }
        return true;
    }
    
    public boolean Help() {
        SendFormatMessage(plugin.MsgHr);
        SendFormatMessage(" &7MCPK ");
        SendFormatMessage("&6/mru alertmsg - Turn On/Off Alert Msg");
        SendFormatMessage("&6/mru pkmsg  - Turn On/Off Time PK Msg");
        if (plugin.hasPermission(sender.getName(),"mcpk.command.leftpk")) {
            SendFormatMessage("&6/mru removepk &7Remove PK Status");
        }
        
        SendFormatMessage(plugin.MsgHr);
        SendFormatMessage(" &7Admin MCPK ");
        SendFormatMessage("&6/mru reload - reload MCPK");

        SendFormatMessage(plugin.MsgHr);
        return true;
    }
    
    public void SendFormatMessage(String msg) {
        sender.sendMessage(plugin.parseColor(msg));
    }
}
