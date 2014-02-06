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
public class AlertMsg extends CommandHandler {

    public AlertMsg(Mcpk plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        
        if(isInvalid(sender, args)) return true;
            
        Player player = (Player) sender;
        
        if(plugin.DB.getAlertMsg(player)) {
            plugin.DB.UpdateAlertMsg(player, Boolean.FALSE);
            plugin.MCPlayers.get(player.getName()).setAlertMsg(Boolean.FALSE);
            SendMessage(plugin.translate.AlertMsgOff);
        }else{
            plugin.DB.UpdateAlertMsg(player, Boolean.TRUE);
            plugin.MCPlayers.get(player.getName()).setAlertMsg(Boolean.TRUE);
            SendMessage(plugin.translate.AlertMsgOn);
        }
                
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        return false;
    }
    
    
}
