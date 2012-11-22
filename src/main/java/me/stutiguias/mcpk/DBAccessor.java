/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import java.sql.Timestamp;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class DBAccessor {
    
    private Boolean UseMySql;
    Mcpk plugin;
    
    public DBAccessor(Mcpk instance) {
       plugin = instance;    
    }
    
    public MCPlayer getPlayer(Player pl) {
        if(getUseMySql()) {
            return plugin.DataBase.getPlayer(pl.getName());
        }else{
            return plugin._FileDB.LoadPlayerFile(pl);
        }
    }
    
    public void CreatePlayer(Player pl,Timestamp Protect) {
        if(getUseMySql()) {
            plugin.DataBase.createPlayer(pl.getName(), "0", 0, Protect); 
        }else{
            plugin._FileDB.CreatePlayer(pl, Protect);
        }
    }
    
    public void UpdateKill(Player pl,int Kills) {
        if(getUseMySql()) {
            plugin.DataBase.UpdateKill(pl.getName(), Kills);
        }else{
            plugin._FileDB.setKills(Kills);
        }
    }

    /**
     * @return the UseMySql
     */
    public Boolean getUseMySql() {
        return UseMySql;
    }

    /**
     * @param UseMySql the UseMySql to set
     */
    public void setUseMySql(Boolean UseMySql) {
        this.UseMySql = UseMySql;
    }
}
