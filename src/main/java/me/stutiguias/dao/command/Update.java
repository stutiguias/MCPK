/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.dao.command;

import me.stutiguias.mcpk.Mcpk;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Daniel
 */
public class Update extends CommandHandler {

    public Update(Mcpk plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        
        if(isInvalid(sender, args)) return true;

        plugin.Update();
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission(sender.getName(),"mcpk.update")) {
            SendMessage("&6You don't have permission");
            return true;
        }
        return false;
    }
    
}
