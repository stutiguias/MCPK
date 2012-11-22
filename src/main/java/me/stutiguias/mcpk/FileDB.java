/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class FileDB {
    
    Mcpk plugin;
    File PlayerFile;
    String Path = "plugins"+ File.separator +"Mcmmorankup"+ File.separator +"userdata";
    YamlConfiguration PlayerYML;
            
    public FileDB(Mcpk instance,Player player,int kills,Date newBieProtectUntil) {
    
        plugin = instance;

        try {
            PlayerFile = new File( Path + File.separator + player.getName() +".yml");
            if(PlayerFile.createNewFile()) {
                LoadYML();
                SetupYML(kills,newBieProtectUntil);
            }else{
                LoadYML();
            }
        } catch (IOException ex) {
            Mcpk.logger.log(Level.INFO, "{0} Error Creating new YML player File", Mcpk.logPrefix);
            Mcpk.logger.log(Level.INFO, "{0}", ex.getMessage());
        }
    }
    
    private void SetupYML(int kills,Date newBieProtectUntil) {
        PlayerYML.set("kills", kills);
        PlayerYML.set("NewbieProtectUntil", newBieProtectUntil);
    }
    
    public void CheckDiretory() {
        File f = new File(Path);
        if(!f.exists())  {
            Mcpk.logger.log(Level.INFO, "{0} Diretory not exist creating new one", Mcpk.logPrefix);
            f.mkdirs();
        }
    }
    
    private void LoadYML() {
        try {
            PlayerYML = new YamlConfiguration();
            PlayerYML.load(PlayerFile);
        } catch (FileNotFoundException ex) {
           Mcpk.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{Mcpk.logPrefix, ex.getMessage()});
        } catch (IOException ex) {
           Mcpk.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{Mcpk.logPrefix, ex.getMessage()});
        } catch (InvalidConfigurationException ex) {
           Mcpk.logger.log(Level.WARNING, "{0} Invalid Configuration {1}", new Object[]{Mcpk.logPrefix, ex.getMessage()});
        }
    }
    
    public void SaveYML() {
        try {
            PlayerYML.save(PlayerFile);
        } catch (FileNotFoundException ex) {
           Mcpk.logger.log(Level.WARNING, "{0} File Not Found {1}", new Object[]{Mcpk.logPrefix, ex.getMessage()});
        } catch (IOException ex) {
           Mcpk.logger.log(Level.WARNING, "{0} IO Problem {1}", new Object[]{Mcpk.logPrefix, ex.getMessage()});
        }
    }
}
