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
    public String protecmsg;
    public String msg;
    public HashMap<Integer, String> pkmsg;
    
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
        msg = Message.getConfig().getString("AlertMessage");
        protecmsg = Message.getConfig().getString("ProtectMessage");
        getMessages();
    }
    
    public void getMessages(){
        pkmsg = new HashMap<Integer, String>();
        ConfigurationSection PKMessage = Message.getConfig().getConfigurationSection("PKMessage");
        for (String key : PKMessage.getKeys(false)){
          pkmsg.put(Integer.parseInt(key), Message.getConfig().getString("PKMessage." + key));
        }
    }
}
