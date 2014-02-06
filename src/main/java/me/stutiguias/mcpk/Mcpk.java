package me.stutiguias.mcpk;

import me.stutiguias.model.MCPlayer;
import me.stutiguias.dao.command.McpkCommandListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.dao.type.DBAccessor;
import me.stutiguias.listeners.*;
import me.stutiguias.metrics.Metrics;
import me.stutiguias.tasks.AlertPkTask;
import me.stutiguias.updater.Updater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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

    public Map<String, MCPlayer> MCPlayers = new HashMap<>();
    
    public DBAccessor DB;
    public Util util;

    public Boolean usenewbieprotect;
    public String NewbieProtectTime;

    public String MsgHr = "&e-----------------------------------------------------";
    
    public int time;
    public int radius;
    public int turnpk;
    
    public Boolean UseTagAPI;
    public Boolean TurnGreenAfterKillPk;
            
    public HashMap<Integer, String> pkbonus = new HashMap<>();
    public Boolean EnableBonusForPK;
    public Boolean ChangePkGroup;
    public Boolean RemoveAllOtherGroup;
    public String GroupPk;
    public Boolean UseScoreBoard;
    public Boolean AlertMsg;
    public Boolean UpdaterNotify;
    public Boolean AlertNewPK;
    
    public String language;
    public Translate translate;
    public ConfigAccessor config;
    
    //Vault
    public Permission permission = null;
    public Economy economy = null;
   
    public static boolean update = false;
    public static String name = "";
    public static String type = "";
    public static String version = "";
    public static String link = "";
    
    public long GetCurrentMilli() {
            return System.currentTimeMillis();
    }
        
    @Override
    public void onEnable(){
        logger.log(Level.INFO,logPrefix + " initializing....");
        
        File dir = getDataFolder();
        if (!dir.exists()) {
          dir.mkdirs();
        }
        onLoadConfig();

        util  = new Util(this);

        getCommand("mcpk").setExecutor(new McpkCommandListener(this));
        
        PluginManager pm = getServer().getPluginManager();
 
        UseTagAPI = getConfig().getBoolean("TagAPI.Use"); 
        TurnGreenAfterKillPk = getConfig().getBoolean("TagAPI.TurnGreenAfterKillPk"); 
        
        if(UseTagAPI) {
            pm.registerEvents(new TagApiPlayerListener(this), this);
            logger.info(logPrefix + " Using TagApi");
        }
        
        // Metrics 
        try {
         logger.log(Level.INFO, "{0} {1} - Sending Metrics, Thank You!", new Object[]{logPrefix, "[Metrics]"});
         Metrics metrics = new Metrics(this);
         metrics.start();
        } catch (IOException e) {
         logger.log(Level.WARNING, "{0} {1} !! Failed to submit the stats !! ", new Object[]{logPrefix, "[Metrics]"});
        }
        
        setupEconomy();
        setupPermissions();
        
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new OnDeathListener(this), this);
        pm.registerEvents(new ProtectListener(this), this);
        
        if(UpdaterNotify){
            Updater updater = new Updater(this, 38364, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
            
            update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
            name = updater.getLatestName(); // Get the latest name
            version = updater.getLatestGameVersion(); // Get the latest game version
            type = updater.getLatestType(); // Get the latest game version
            link = updater.getLatestFileLink(); // Get the latest link
        }
        logger.log(Level.INFO,logPrefix + " done.");
    }
    
    public void onLoadConfig() {
        try{
            config = new ConfigAccessor(this,"config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();
            
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 2){ 
                config.MakeOld();
                config.setupConfig();
                fc = config.getConfig();
            }
            
            long AlertPKFrequency = fc.getLong("AboutPK.AlertFrequency");
            getServer().getScheduler().runTaskTimerAsynchronously(this, new AlertPkTask(this), AlertPKFrequency, AlertPKFrequency);
            
            time                    =fc.getInt("AboutPK.TimeOn") * 1000;
            radius                  =fc.getInt("AboutPK.Radius");
            turnpk                  =fc.getInt("AboutPK.HowMuchForTurn");
            ChangePkGroup           =fc.getBoolean("AboutPK.ChangeGroupIf");
            GroupPk                 =fc.getString("AboutPK.NewGroup");
            RemoveAllOtherGroup     =fc.getBoolean("AboutPK.RemoveAllOthersGroup");
            UseScoreBoard           =fc.getBoolean("AboutPK.UseScoreBoard");
            AlertMsg                =fc.getBoolean("AboutPK.Alert");
            UpdaterNotify           =fc.getBoolean("UpdaterNotify");
            EnableBonusForPK        =fc.getBoolean("AboutPK.Bonus.Enable");
            AlertNewPK              =fc.getBoolean("AboutPK.AlertNewPK");
            
            if(EnableBonusForPK) GetBonusForPK();
            
            NewbieProtectTime       =fc.getString("AboutPlayer.NewBieProtect.Enable");
            usenewbieprotect        =fc.getBoolean("AboutPlayer.NewBieProtect.NewBieProtectTime");

            language = fc.getString("Language");
            translate = new Translate(this, language);
            
            String dbHost       = fc.getString("MySQL.Host");
            String dbUser       = fc.getString("MySQL.Username");
            String dbPass       = fc.getString("MySQL.Password");
            String dbPort       = fc.getString("MySQL.Port");
            String dbDatabase   = fc.getString("MySQL.Database");
            boolean dbMySqlUse  = fc.getBoolean("MySQL.Use");

            DB = new DBAccessor(this,dbMySqlUse,dbHost,dbUser,dbPass,dbPort,dbDatabase);
            
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void GetBonusForPK() {
        pkbonus = new HashMap<>();
        for (String key : config.getConfig().getConfigurationSection("AboutPK.Bonus.OnKill").getKeys(false)){
          Integer qtdDeaths = Integer.parseInt(key);
          String Bonus = getConfig().getString("AboutPK.Bonus.OnKill." + key);
          pkbonus.put(qtdDeaths,Bonus);
          logger.log(Level.INFO, logPrefix + " Bonus PK-kill {0} set to {1}", new Object[]{key, getConfig().getString("AboutPK.Bonus.OnKill." + key)});
        }
    }
    
    @Override
    public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		logger.log(Level.INFO, logPrefix + " Disabled.");
    }
    
    public void OnReload() {
        config.reloadConfig();
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
    
    public boolean hasPermission(Player player, String Permission) {
        return permission.has(player.getWorld(), player.getName(), Permission.toLowerCase());
    }
    
    public void Update() {
        Updater updater = new Updater(this, 38364, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
    }
    
}
