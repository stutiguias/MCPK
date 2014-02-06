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
public class PkMsg extends CommandHandler {

    public PkMsg(Mcpk plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        
        Player player = (Player)sender;
        
        if(plugin.DB.getPKMsg(player)) {
            plugin.DB.UpdatePKMsg(player, Boolean.FALSE);
            plugin.MCPlayers.get(player.getName()).setPKMsg(Boolean.FALSE);
            SendMessage(plugin.translate.TimePkMsgOff);
        }else{
            plugin.DB.UpdatePKMsg(player, Boolean.TRUE);
            plugin.MCPlayers.get(player.getName()).setPKMsg(Boolean.TRUE);
            SendMessage(plugin.translate.TimePkMsgOn);
        }
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
