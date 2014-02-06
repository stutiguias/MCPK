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
public class RemovePK extends CommandHandler {

    public RemovePK(Mcpk plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        
        if(isInvalid(sender, args)) return true;
        
        Player player = (Player)sender;
        
        plugin.MCPlayers.get(player.getName()).setKills(0);
        plugin.MCPlayers.get(player.getName()).setIsPK(Boolean.FALSE);
        SendMessage(plugin.translate.YouRemovePKStatus);
        
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission(sender.getName(),"mcpk.command.leftpk")) {
            SendMessage("&6You don't have permission");
            return true;
        }
        return false;
    }
    
}
