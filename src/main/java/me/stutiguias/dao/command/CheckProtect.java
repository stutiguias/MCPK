/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.dao.command;

import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.model.MCPlayer;    
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class CheckProtect extends CommandHandler {

    public CheckProtect(Mcpk plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        
        if(isInvalid(sender, args)) return true;
        
        if(args.length <= 1) {
            Check();
        }else{
            CheckOthers(args[1]);
        }
        
        return true;
    }

    private void Check() {
        Player player = (Player)sender;
        MCPlayer mcPlayer = plugin.DB.getPlayer(player);
        SendMessage(MsgHr);
        if(mcPlayer != null)
            SendMessage("Protected until " + mcPlayer.getNewBieProtectUntil());
        else
            SendMessage("&4Not Found");
        SendMessage(MsgHr);
    }
    
    private void CheckOthers(String name){
        Player player = plugin.getServer().getPlayer(name);
        
        SendMessage(MsgHr);
        if(player == null) {
             SendMessage("&4Not Found");
            return;
        }
        MCPlayer mcPlayer = plugin.DB.getPlayer(player);
        if(mcPlayer != null)
            SendMessage("Protected until " + mcPlayer.getNewBieProtectUntil());
        else
            SendMessage("&4Not Found");
        SendMessage(MsgHr);
    }
    
    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission(sender.getName(),"mcpk.command.check")) {
            SendMessage("&6You don't have permission");
            return true;
        }
        return false;
    }
    
}
