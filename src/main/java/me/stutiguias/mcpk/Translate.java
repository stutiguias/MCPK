/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Daniel
 */
public class Translate {
    
    public ConfigAccessor Message;
    public String ProtectMsg;
    public String Msg;
    public HashMap<Integer, String> PkMsg;
    
    public Translate(Mcpk instance,String language) {
        Message = new ConfigAccessor(instance,language + ".yml");
        Init();
        LoadConfig();
        Mcpk.logger.log(Level.INFO, Mcpk.logPrefix + " Your lang is {0}", new Object[]{ language });
    }
    
    private void Init() {
        try{
            Message.setupConfig();    
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        Message.saveConfig();
        Message.reloadConfig();
    }
    
    private void LoadConfig() {
        Msg = Message.getConfig().getString("AlertMessage");
        ProtectMsg = Message.getConfig().getString("ProtectMessage");
        GetMessages();
    }
    
    private void GetMessages(){
        PkMsg = new HashMap<>();
        ConfigurationSection PKMessage = Message.getConfig().getConfigurationSection("PKMessage");
        for (String key : PKMessage.getKeys(false)){
          PkMsg.put(Integer.parseInt(key), Message.getConfig().getString("PKMessage." + key));
        }
    }
}
