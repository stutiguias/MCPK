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
        if (args.length == 0) return Help(player);
        
        switch(args[0]) {
            case "reload":
                 if(!plugin.hasPermission(sender.getName(),"mcpk.command.reload")) return false;
                 return Reload(sender);
            case "alertmsg":
                return AlertMsg(player);
            case "pkmsg":
                return PKMsg(player);
            case "removepk":
                if(!plugin.hasPermission(sender.getName(),"mcpk.command.leftpk")) return false;
                RemovePkStatus(player);
                return true;
            case "update":
                if(!plugin.hasPermission(sender.getName(),"mcpk.update")) return false;
                return Update();
            case "help":
            case "?":
            default:
                return Help(player);
        }
    }
    
    public boolean Update() {
        plugin.Update();
        return true;
    }
    
    public void RemovePkStatus(Player player) {
        plugin.MCPlayers.get(player.getName()).setKills(0);
        plugin.MCPlayers.get(player.getName()).setIsPK(Boolean.FALSE);
        SendFormatMessage(plugin.translate.YouRemovePKStatus);
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
            SendFormatMessage(plugin.translate.AlertMsgOff);
        }else{
            plugin.DB.UpdateAlertMsg(player, Boolean.TRUE);
            plugin.MCPlayers.get(player.getName()).setAlertMsg(Boolean.TRUE);
            SendFormatMessage(plugin.translate.AlertMsgOn);
        }
        return true;
    }
    
    public boolean PKMsg(Player player) {
        if(plugin.DB.getPKMsg(player)) {
            plugin.DB.UpdatePKMsg(player, Boolean.FALSE);
            plugin.MCPlayers.get(player.getName()).setPKMsg(Boolean.FALSE);
            SendFormatMessage(plugin.translate.TimePkMsgOff);
        }else{
            plugin.DB.UpdatePKMsg(player, Boolean.TRUE);
            plugin.MCPlayers.get(player.getName()).setPKMsg(Boolean.TRUE);
            SendFormatMessage(plugin.translate.TimePkMsgOn);
        }
        return true;
    }
    
    public boolean Help(Player player) {
        SendFormatMessage(plugin.MsgHr);
        SendFormatMessage(" &7MCPK ");
        
        plugin.DB.getPlayer(player);
        
        if(plugin.DB.getAlertMsg(player))
            SendFormatMessage("&6/mcpk alertmsg - Turn &cOff&6 Alert Msg");
        else
            SendFormatMessage("&6/mcpk alertmsg - Turn &cOn&6 Alert Msg");
        
        if(plugin.DB.getPKMsg(player))
            SendFormatMessage("&6/mcpk pkmsg  - Turn &cOff&6 Time PK Msg");
        else
            SendFormatMessage("&6/mcpk pkmsg  - Turn &cOn&6 Time PK Msg");
        
        if (plugin.hasPermission(player,"mcpk.command.leftpk")) {
            SendFormatMessage("&6/mcpk removepk - Remove PK Status");
        }
        
        if(plugin.hasPermission(player,"mcpk.command.reload")) {
            SendFormatMessage(plugin.MsgHr);
            SendFormatMessage(" &7Admin MCPK ");
            if (plugin.hasPermission(player,"mcpk.update")) {
                SendFormatMessage("&6/mcpk update - Update plugin");
            }
            SendFormatMessage("&6/mcpk reload - reload MCPK");
        }
        
        SendFormatMessage(plugin.MsgHr);
        return true;
    }
    
    public void SendFormatMessage(String msg) {
        sender.sendMessage(plugin.parseColor(msg));
    }
}
