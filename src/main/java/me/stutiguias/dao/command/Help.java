/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.dao.command;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class Help extends CommandHandler {

    public Help(Mcpk plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        
        if(isInvalid(sender, args)) return true;
        
        Player player = (Player)sender;
        
        SendMessage(plugin.MsgHr);
        SendMessage(" &7MCPK ");
        
        plugin.DB.getPlayer(player);
        
        if(plugin.DB.getAlertMsg(player))
            SendMessage("&6/mcpk alertmsg - Turn &cOff&6 Alert Msg");
        else
            SendMessage("&6/mcpk alertmsg - Turn &cOn&6 Alert Msg");
        
        if(plugin.DB.getPKMsg(player))
            SendMessage("&6/mcpk pkmsg  - Turn &cOff&6 Time PK Msg");
        else
            SendMessage("&6/mcpk pkmsg  - Turn &cOn&6 Time PK Msg");
        
        if (plugin.hasPermission(player,"mcpk.command.leftpk")) {
            SendMessage("&6/mcpk removepk - Remove PK Status");
        }
        
        if (plugin.hasPermission(player, "mcpk.command.check")) {
            SendMessage("&6/mcpk checkprotect - Check if you is protected");
            SendMessage("&6/mcpk checkprotect <name> - Check protected");
        }
        
        if(plugin.hasPermission(player,"mcpk.command.reload")) {
            SendMessage(plugin.MsgHr);
            SendMessage(" &7Admin MCPK ");
            if (plugin.hasPermission(player,"mcpk.update")) {
                SendMessage("&6/mcpk update - Update plugin");
            }
            SendMessage("&6/mcpk reload - reload MCPK");
        }
        
        SendMessage(plugin.MsgHr);
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        return false;
    }
    
}
