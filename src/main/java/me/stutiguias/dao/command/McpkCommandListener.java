/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.command;

import java.util.HashMap;
import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.mcpk.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class McpkCommandListener extends Util implements CommandExecutor {
    
    private final HashMap<String,CommandHandler> avaibleCommands;
    
    public McpkCommandListener(Mcpk instance)
    {
        super(instance);
        avaibleCommands = new HashMap<>();
        
        avaibleCommands.put("help", new Help(plugin));
        avaibleCommands.put("alertmsg", new AlertMsg(plugin));
        avaibleCommands.put("pkmsg", new PkMsg(plugin));
        avaibleCommands.put("reload", new Reload(plugin));
        avaibleCommands.put("removepk", new RemovePK(plugin));
        avaibleCommands.put("checkprotect", new CheckProtect(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        this.sender = sender;
        if (sender.getName().equalsIgnoreCase("CONSOLE")) return true;
        if (!(sender instanceof Player)) return false;
 
        if(args.length < 0 || args.length == 0) return CommandNotFound();
        
        String executedCommand = args[0].toLowerCase();

        if(avaibleCommands.containsKey(executedCommand))
            return avaibleCommands.get(executedCommand).OnCommand(sender,args);
        else
            return CommandNotFound();     
    } 
    
    private boolean CommandNotFound() {
        SendMessage("&eCommand not found try /mcpk help ");
        return true;
    }
}
