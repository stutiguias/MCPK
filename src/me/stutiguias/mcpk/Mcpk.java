package me.stutiguias.mcpk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.dao.mysql.MySql;
import me.stutiguias.listeners.McpkPlayerListener;
import me.stutiguias.tasks.AlertPkTask;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Stutiguias
 */
public class Mcpk extends JavaPlugin{
    
    public static final String logPrefix = "[MCPK]";
    public static final Logger log = Logger.getLogger("Minecraft");
    
    public final McpkPlayerListener playerlistener = new McpkPlayerListener(this);
    
    public MySql DataBase;
    
    public Map<String, PK> IsPk = new HashMap<String, PK>();
   
    public String msg;
    
    public boolean alertaboutpk;
    public int time;
    public int radius;
    public HashMap<Integer, String> pkmsg = new HashMap<Integer, String>();
    
    public Mcpk(){
   
    }
    
    public long getCurrentMilli() {
            return System.currentTimeMillis();
    }
        
    @Override
    public void onEnable(){
        log.log(Level.INFO,logPrefix + " initializing....");

	initConfig();
        alertaboutpk = getConfig().getBoolean("Basic.AlertAboutPK");
        time = getConfig().getInt("Basic.Time");
        radius = getConfig().getInt("Basic.Radius");
        long AlertPKFrequency = getConfig().getLong("Basic.AlertPKFrequency");
        msg = getConfig().getString("Basic.AlertMessage");
        String dbHost = getConfig().getString("MySQL.Host");
        String dbUser = getConfig().getString("MySQL.Username");
        String dbPass = getConfig().getString("MySQL.Password");
        String dbPort = getConfig().getString("MySQL.Port");
        String dbDatabase = getConfig().getString("MySQL.Database");
        getMessages();
        if(!dbPass.equalsIgnoreCase("password123")) {
          DataBase = new MySql(dbHost,dbUser,dbPass,dbPort,dbDatabase);
          DataBase.InitTables();
        }else{
          log.log(Level.INFO,logPrefix + " Configure your MYSQL table.");
          onDisable();
        }
        
        if(alertaboutpk) {
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, new AlertPkTask(this), AlertPKFrequency, AlertPKFrequency);
        }
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerlistener, this);
        
        log.log(Level.INFO,logPrefix + " done.");
    }
    
    public HashMap getMessages(){
        pkmsg = new HashMap<Integer, String>();
        for (String key : getConfig().getConfigurationSection("Message.").getKeys(false)){
          pkmsg.put(Integer.parseInt(key), getConfig().getString("Message." + key));
          log.log(Level.INFO, logPrefix + "Kill Number " + key + " set to " + getConfig().getString("Message." + key));
        }
        return pkmsg;
    }
    @Override
    public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		log.log(Level.INFO, logPrefix + " Disabled.");
	}
    
    private void initConfig() {
		getConfig().addDefault("MySQL.Host", "localhost");
		getConfig().addDefault("MySQL.Username", "root");
		getConfig().addDefault("MySQL.Password", "password123");
		getConfig().addDefault("MySQL.Port", "3306");
		getConfig().addDefault("MySQL.Database", "minecraft");
                getConfig().addDefault("Basic.AlertAboutPK", true);
		getConfig().addDefault("Basic.Time", 5000);
                getConfig().addDefault("Basic.Radius", 10);
                getConfig().addDefault("Basic.AlertPKFrequency", 30L);
                getConfig().addDefault("Basic.AlertMessage", "%player% is PK and is NEAR YOU");
                pkmsg = new HashMap<Integer, String>();
                pkmsg.put(2, "%player% first message");
                pkmsg.put(3, "%player% second message");
                pkmsg.put(4, "%player% third message"); 
                getConfig().addDefault("Message", pkmsg);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
