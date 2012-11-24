/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Daniel
 */
public class Translate {
    
    public ConfigAccessor Message;
    public Mcpk plugin;
    public String protecmsg;
    public String msg;
    public HashMap<Integer, String> pkmsg;
    
    public Translate(Mcpk instance,String language) {
        plugin = instance;
        Message = new ConfigAccessor(plugin,language + ".yml");
        Init();
        LoadConfig();
    }
    
    private void Init() {
        Message.getConfig().addDefault("AlertMessage", "%player% is PK and is NEAR YOU");
        Message.getConfig().addDefault("ProtectMessage", "You r protect for %d%! Until %date%!");
        pkmsg = new HashMap<Integer, String>();
        pkmsg.put(2, "%player% first message");
        pkmsg.put(3, "%player% second message");
        pkmsg.put(4, "%player% third message"); 
        Message.getConfig().addDefault("PKMessage", pkmsg);
        Message.getConfig().options().copyDefaults(true);
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
          Mcpk.logger.log(Level.INFO, Mcpk.logPrefix + " Kill Number {0} set to {1}", new Object[]{key, Message.getConfig().getString("PKMessage." + key)});
        }
    }
}
