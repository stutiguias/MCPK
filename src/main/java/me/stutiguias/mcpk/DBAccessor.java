/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import java.sql.Timestamp;
import me.stutiguias.dao.mysql.MySql;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class DBAccessor {
    
    private Boolean UseMySql;
    Mcpk plugin;
    private FileDB _FileDB;
    private MySql DataBase;
    
    public DBAccessor(Mcpk instance,Boolean UseMySql,String dbHost,String dbUser,String dbPass,String dbPort,String dbDatabase) {
        plugin = instance;    
        this.UseMySql = UseMySql;
        if(UseMySql) {
            DataBase = new MySql(dbHost,dbUser,dbPass,dbPort,dbDatabase);
            DataBase.InitTables();
        }else{
            _FileDB = new FileDB();
            _FileDB.CheckDiretory();
        }
    }
    
    public MCPlayer getPlayer(Player pl) {
        if(getUseMySql()) {
            return DataBase.getPlayer(pl.getName());
        }else{
            return _FileDB.LoadPlayerFile(pl);
        }
    }
    
    public void CreatePlayer(Player pl,Timestamp Protect) {
        if(getUseMySql()) {
            DataBase.createPlayer(pl.getName(), "0", 0, Protect); 
        }else{
            _FileDB.CreatePlayer(pl, Protect);
        }
    }
    
    public void UpdateKill(Player pl,int Kills) {
        if(getUseMySql()) {
            DataBase.UpdateKill(pl.getName(), Kills);
        }else{
            _FileDB.setKills(Kills);
        }
    }

    public Boolean SetPKMsg(Player pl,Boolean PKMsg) {
        if(getUseMySql()) {
            Integer index = DataBase.getPlayer(pl.getName()).getIndex();
            DataBase.SetDetails(index,"PKMsg",PKMsg.toString());
            return true;
        }else{
            _FileDB.setPKMsg(PKMsg);
            return true;
        }
    }
    
    public Boolean UpdatePKMsg(Player pl,Boolean PKMsg) {
        if(getUseMySql()) {
            Integer index = DataBase.getPlayer(pl.getName()).getIndex();
            DataBase.UpdateDetails(index,"PKMsg",PKMsg.toString());
            return true;
        }else{
            _FileDB.setPKMsg(PKMsg);
            return true;
        }
    }
    
    public Boolean getPKMsg(Player pl) {
        if(getUseMySql()) {
            Integer index = DataBase.getPlayer(pl.getName()).getIndex();
            return Boolean.parseBoolean(DataBase.GetDetails(index,"PKMsg"));
        }else{
            return _FileDB.getPKMsg();
        }
    }
    
    public Boolean SetAlertMsg(Player pl,Boolean AlertMsg) {
        if(getUseMySql()) {
            Integer index = DataBase.getPlayer(pl.getName()).getIndex();
            DataBase.SetDetails(index,"AlertMsg",AlertMsg.toString());
            return true;
        }else{
            _FileDB.setAlertMsg(AlertMsg);
            return true;
        }
    }
    
    public Boolean UpdateAlertMsg(Player pl,Boolean AlertMsg) {
        if(getUseMySql()) {
            Integer index = DataBase.getPlayer(pl.getName()).getIndex();
            DataBase.UpdateDetails(index,"AlertMsg",AlertMsg.toString());
            return true;
        }else{
            _FileDB.setAlertMsg(AlertMsg);
            return true;
        }
    }
    
    public Boolean getAlertMsg(Player pl) {
        if(getUseMySql()) {
            Integer index = DataBase.getPlayer(pl.getName()).getIndex();
            return Boolean.parseBoolean(DataBase.GetDetails(index,"AlertMsg"));
        }else{
            return _FileDB.getAlertMsg();
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
