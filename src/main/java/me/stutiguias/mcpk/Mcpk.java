package me.stutiguias.mcpk;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.dao.mysql.MySql;
import me.stutiguias.listeners.MCPKCommandListener;
import me.stutiguias.listeners.McpkPlayerListener;
import me.stutiguias.tasks.AlertPkTask;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    public Boolean usenewbieprotect;
    public int newbieprotectdays;
    public String protecmsg;
    public boolean alertaboutpk;
    public int time;
    public int radius;
    public int turnpk;
    public HashMap<Integer, String> pkmsg = new HashMap<Integer, String>();
    public Boolean ChangePkGroup;
    public String GroupPk;
    //Vault
    public Permission permission = null;
    public Economy economy = null;
    
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
        time = getConfig().getInt("Basic.Time") * 1000;
        radius = getConfig().getInt("Basic.Radius");
        turnpk = getConfig().getInt("Basic.HowMuchForTurnPk");
        long AlertPKFrequency = getConfig().getLong("Basic.AlertPKFrequency");
        msg = getConfig().getString("Basic.AlertMessage");
        ChangePkGroup = getConfig().getBoolean("Basic.ChangeGroupIfPK");
        GroupPk = getConfig().getString("Basic.WhatGroupChangePK");
        
        newbieprotectdays = getConfig().getInt("Protect.NewBieProtectDays");
        protecmsg = getConfig().getString("Protect.Message");
        usenewbieprotect = getConfig().getBoolean("Protect.UseNewBieProtect");
        String dbHost = getConfig().getString("MySQL.Host");
        String dbUser = getConfig().getString("MySQL.Username");
        String dbPass = getConfig().getString("MySQL.Password");
        String dbPort = getConfig().getString("MySQL.Port");
        String dbDatabase = getConfig().getString("MySQL.Database");
        getMessages();
        getCommand("mcpk").setExecutor(new MCPKCommandListener(this));
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
        
        // Setup Vault 
        setupEconomy();
        setupPermissions();
        
        log.log(Level.INFO,logPrefix + " done.");
    }
    
    public HashMap getMessages(){
        pkmsg = new HashMap<Integer, String>();
        for (String key : getConfig().getConfigurationSection("Message.").getKeys(false)){
          pkmsg.put(Integer.parseInt(key), getConfig().getString("Message." + key));
          log.log(Level.INFO, logPrefix + "Kill Number {0} set to {1}", new Object[]{key, getConfig().getString("Message." + key)});
        }
        return pkmsg;
    }
    
    @Override
    public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		log.log(Level.INFO, logPrefix + " Disabled.");
	}
    
    public void OnReload() {
        this.reloadConfig();
        saveConfig();
        getServer().getPluginManager().disablePlugin(this);
        getServer().getPluginManager().enablePlugin(this);
    }
    
    private void initConfig() {
		getConfig().addDefault("MySQL.Host", "localhost");
		getConfig().addDefault("MySQL.Username", "root");
		getConfig().addDefault("MySQL.Password", "password_here");
		getConfig().addDefault("MySQL.Port", "3306");
		getConfig().addDefault("MySQL.Database", "minecraft");
                getConfig().addDefault("Basic.AlertAboutPK", true);
		getConfig().addDefault("Basic.Time", 25);
                getConfig().addDefault("Basic.Radius", 10);
                getConfig().addDefault("Basic.AlertPKFrequency", 30L);
                getConfig().addDefault("Basic.HowMuchForTurnPk", 3);
                getConfig().addDefault("Basic.AlertMessage", "%player% is PK and is NEAR YOU");
                getConfig().addDefault("Basic.ChangeGroupIfPK",false);
                getConfig().addDefault("Basic.WhatGroupChangePK","pk");
                getConfig().addDefault("Protect.UseNewBieProtect",true);
                getConfig().addDefault("Protect.NewBieProtectDays",2);
                getConfig().addDefault("Protect.Message", "You r protect for %d% days! Until %date%!");
                pkmsg = new HashMap<Integer, String>();
                pkmsg.put(2, "%player% first message");
                pkmsg.put(3, "%player% second message");
                pkmsg.put(4, "%player% third message"); 
                getConfig().addDefault("Message", pkmsg);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
    
        private boolean setupPermissions() {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            permission = rsp.getProvider();
            return permission != null;
        }

	private Boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null);
	}
        
}