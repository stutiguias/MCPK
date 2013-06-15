package me.stutiguias.mcpk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.listeners.*;
import me.stutiguias.tasks.AlertPkTask;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
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
    public Boolean TurnGreenAfterKillPk;
            
    public HashMap<Integer, String> pkbonus = new HashMap<Integer, String>();
    public Boolean UseBonusForPK;
    public Boolean ChangePkGroup;
    public Boolean RemoveAllOtherGroup;
    public String GroupPk;
    
    public String language;
    public Translate translate;
    public ConfigAccessor config;
    
    //Vault
    public Permission permission = null;
    public Economy economy = null;
   
    public long GetCurrentMilli() {
            return System.currentTimeMillis();
    }
        
    @Override
    public void onEnable(){
        logger.log(Level.INFO,logPrefix + " initializing....");
        try{
            config = new ConfigAccessor(this,"config.yml");
            config.setupConfig();
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        
        FileConfiguration fc = config.getConfig();
        
        comuns  = new Comuns();
        
        alertaboutpk = fc.getBoolean("Basic.AlertAboutPK");
        if(alertaboutpk) {
            long AlertPKFrequency = getConfig().getLong("Basic.AlertPKFrequency");
            getServer().getScheduler().runTaskTimerAsynchronously(this, new AlertPkTask(this), AlertPKFrequency, AlertPKFrequency);
        }
        time =         fc.getInt("Basic.TimeOnPK") * 1000;
        radius =       fc.getInt("Basic.Radius");
        turnpk =       fc.getInt("Basic.HowMuchForTurnPk");

        ChangePkGroup =         fc.getBoolean("Basic.ChangeGroupIfPK");
        GroupPk =               fc.getString("Basic.WhatGroupChangePK");
        UseBonusForPK =         fc.getBoolean("Bonus.UseBonusForPk");
        RemoveAllOtherGroup =   fc.getBoolean("Basic.RemoveAllOthersGroup");
        
        NewbieProtectTime =     fc.getString("Protect.NewBieProtectTime");
        usenewbieprotect =      fc.getBoolean("Protect.UseNewBieProtect");
        
        String dbHost       = fc.getString("MySQL.Host");
        String dbUser       = fc.getString("MySQL.Username");
        String dbPass       = fc.getString("MySQL.Password");
        String dbPort       = fc.getString("MySQL.Port");
        String dbDatabase   = fc.getString("MySQL.Database");
        boolean dbMySqlUse  = fc.getBoolean("MySQL.Use");
        
        DB = new DBAccessor(dbMySqlUse,dbHost,dbUser,dbPass,dbPort,dbDatabase);
             
        language = getConfig().getString("Basic.Language");
        translate = new Translate(this, language);
        
        GetBonusForPK();
        
        getCommand("mcpk").setExecutor(new MCPKCommandListener(this));
        
        PluginManager pm = getServer().getPluginManager();
 
        UseTagAPI = getConfig().getBoolean("TagAPI.Use"); 
        TurnGreenAfterKillPk = getConfig().getBoolean("TagAPI.TurnGreenAfterKillPk"); 
        
        if(UseTagAPI) {
            pm.registerEvents(new TagApiPlayerListener(this), this);
            logger.info(logPrefix + " Using TagApi");
        }
   
        setupEconomy();
        setupPermissions();
        
        pm.registerEvents(new McpkPlayerListener(this), this);
        pm.registerEvents(new McpkOnDeathListener(this), this);
        pm.registerEvents(new McpkProtectListener(this), this);
        
        logger.log(Level.INFO,logPrefix + " done.");
    }
    
    public void GetBonusForPK() {
        pkbonus = new HashMap<Integer, String>();
        for (String key : config.getConfig().getConfigurationSection("Bonus.ForPkOnTime").getKeys(false)){
          Integer qtdDeaths = Integer.parseInt(key);
          String Bonus = getConfig().getString("Bonus.ForPkOnTime." + key);
          pkbonus.put(qtdDeaths,Bonus);
          logger.log(Level.INFO, logPrefix + " Bonus PK-kill {0} set to {1}", new Object[]{key, getConfig().getString("Bonus.ForPkOnTime." + key)});
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
    
    public boolean hasPermission(String PlayerName,String Permission) {
       return permission.has(getServer().getPlayer(PlayerName).getWorld(),PlayerName,Permission);
    }
}
