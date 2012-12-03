package me.stutiguias.mcpk;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.listeners.*;
import me.stutiguias.tasks.AlertPkTask;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Stutiguias
 */
public class Mcpk extends JavaPlugin{
    
    public static final String logPrefix = "[MCPK]";
    public static final Logger logger = Logger.getLogger("Minecraft");

    public Map<String, MCPlayer> MCPlayers = new HashMap<String, MCPlayer>();
    
    public DBAccessor DB;
    public Comuns comuns;

    public Boolean usenewbieprotect;
    public String NewbieProtectTime;

    public boolean alertaboutpk;
    public int time;
    public int radius;
    public int turnpk;
    public Boolean UseTagAPI;

    public HashMap<Integer, String> pkbonus = new HashMap<Integer, String>();
    public Boolean UseBonusForPK;
    public Boolean ChangePkGroup;
    public Boolean RemoveAllOtherGroup;
    public String GroupPk;
    
    public String language;
    public Translate translate;
    
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
        logger.log(Level.INFO,logPrefix + " initializing....");

	initConfig();
        
        comuns  = new Comuns();
        
        alertaboutpk = getConfig().getBoolean("Basic.AlertAboutPK");
        time = getConfig().getInt("Basic.TimeOnPK") * 1000;
        radius = getConfig().getInt("Basic.Radius");
        turnpk = getConfig().getInt("Basic.HowMuchForTurnPk");
        long AlertPKFrequency = getConfig().getLong("Basic.AlertPKFrequency");

        ChangePkGroup = getConfig().getBoolean("Basic.ChangeGroupIfPK");
        GroupPk = getConfig().getString("Basic.WhatGroupChangePK");
        UseBonusForPK = getConfig().getBoolean("Bonus.UseBonusForPk");
        RemoveAllOtherGroup = getConfig().getBoolean("Basic.RemoveAllOthersGroup");
        
        NewbieProtectTime = getConfig().getString("Protect.NewBieProtectTime");
        usenewbieprotect = getConfig().getBoolean("Protect.UseNewBieProtect");
        
        String dbHost = getConfig().getString("MySQL.Host");
        String dbUser = getConfig().getString("MySQL.Username");
        String dbPass = getConfig().getString("MySQL.Password");
        String dbPort = getConfig().getString("MySQL.Port");
        String dbDatabase = getConfig().getString("MySQL.Database");
        DB = new DBAccessor(this,getConfig().getBoolean("MySQL.Use"),dbHost,dbUser,dbPass,dbPort,dbDatabase);
             
        language = getConfig().getString("Basic.Language");
        translate = new Translate(this, language);
        
        getBonusForPK();
        
        getCommand("mcpk").setExecutor(new MCPKCommandListener(this));
        
        if(alertaboutpk) {
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, new AlertPkTask(this), AlertPKFrequency, AlertPKFrequency);
        }
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(new McpkPlayerListener(this), this);
        pm.registerEvents(new McpkOnDeathListener(this), this);
        pm.registerEvents(new McpkProtectListener(this), this);

        // TagAPI Config
        UseTagAPI = getConfig().getBoolean("UseTagAPI"); 
        if(UseTagAPI) {
            pm.registerEvents(new TagApiPlayerListener(this), this);
            logger.info(logPrefix + " Using TagApi");
        }
       
        // Setup Vault 
        setupEconomy();
        setupPermissions();
        
        logger.log(Level.INFO,logPrefix + " done.");
    }
    
    public void getBonusForPK() {
        pkbonus = new HashMap<Integer, String>();
        for (String key : getConfig().getConfigurationSection("Bonus.ForPkOnTime").getKeys(false)){
          pkbonus.put(Integer.parseInt(key), getConfig().getString("Bonus.ForPkOnTime." + key));
          logger.log(Level.INFO, logPrefix + " Bonus for PK kill number {0} set to {1}", new Object[]{key, getConfig().getString("Bonus.ForPkOnTime." + key)});
        }
    }
    
    @Override
    public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		logger.log(Level.INFO, logPrefix + " Disabled.");
	}
    
    public void OnReload() {
        this.reloadConfig();
        saveConfig();
        getServer().getPluginManager().disablePlugin(this);
        getServer().getPluginManager().enablePlugin(this);
    }
    
    private void initConfig() {
                
                getConfig().addDefault("MySQL.Use", false);
		getConfig().addDefault("MySQL.Host", "localhost");
		getConfig().addDefault("MySQL.Username", "root");
		getConfig().addDefault("MySQL.Password", "password_here");
		getConfig().addDefault("MySQL.Port", "3306");
		getConfig().addDefault("MySQL.Database", "minecraft");
                
                getConfig().addDefault("Basic.AlertAboutPK", true);
		getConfig().addDefault("Basic.TimeOnPK", 25);
                getConfig().addDefault("Basic.Radius", 10);
                getConfig().addDefault("Basic.AlertPKFrequency", 30L);
                getConfig().addDefault("Basic.HowMuchForTurnPk", 3);
                getConfig().addDefault("Basic.ChangeGroupIfPK",false);
                getConfig().addDefault("Basic.RemoveAllOthersGroup", false);
                getConfig().addDefault("Basic.WhatGroupChangePK","pk");
                getConfig().addDefault("Basic.Language","eng");
                
                getConfig().addDefault("UseTagAPI",false);
                
                getConfig().addDefault("Bonus.UseBonusForPk",false);
                pkbonus = new HashMap<Integer, String>();
                pkbonus.put(2, "34,35,36");
                pkbonus.put(3, "45");
                pkbonus.put(4, "56,57,58");
                getConfig().addDefault("Bonus.ForPkOnTime", pkbonus);
                
                getConfig().addDefault("Protect.UseNewBieProtect",true);
                getConfig().addDefault("Protect.NewBieProtectTime","10m");
             
		getConfig().options().copyDefaults(true);
		saveConfig();
                reloadConfig();
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
        
        public String parseColor(String message) {
             for (ChatColor color : ChatColor.values()) {
                message = message.replaceAll(String.format("&%c", color.getChar()), color.toString());
            }
            return message;
        }
}
