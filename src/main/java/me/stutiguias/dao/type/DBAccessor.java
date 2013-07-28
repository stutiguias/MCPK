/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.type;

import java.sql.Timestamp;
import me.stutiguias.mcpk.MCPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class DBAccessor {
    
    private Boolean UseMySql;
    private FileDB _fileDB;
    private MySqlDB _mySqlDB;
    
    public DBAccessor(Boolean UseMySql,String dbHost,String dbUser,String dbPass,String dbPort,String dbDatabase) {  
        this.UseMySql = UseMySql;
        if(UseMySql) {
            _mySqlDB = new MySqlDB(dbHost,dbUser,dbPass,dbPort,dbDatabase);
            _mySqlDB.InitTables();
        }else{
            _fileDB = new FileDB();
            _fileDB.CheckDiretory();
        }
    }
    
    public MCPlayer getPlayer(Player pl) {
        if(getUseMySql()) {
            return _mySqlDB.getPlayer(pl.getName());
        }else{
            return _fileDB.LoadPlayerFile(pl);
        }
    }
    
    public void CreatePlayer(Player pl,Timestamp Protect) {
        if(getUseMySql()) {
            _mySqlDB.createPlayer(pl.getName(), "0", 0, Protect); 
        }else{
            _fileDB.CreatePlayer(pl, Protect);
        }
    }
    
    public void UpdateKill(Player pl,int Kills) {
        if(getUseMySql()) {
            _mySqlDB.UpdateKill(pl.getName(), Kills);
        }else{
            _fileDB.setKills(Kills);
        }
    }

    public Boolean SetPKMsg(Player pl,Boolean PKMsg) {
        if(getUseMySql()) {
            Integer index = _mySqlDB.getPlayer(pl.getName()).getIndex();
            _mySqlDB.SetDetails(index,"PKMsg",PKMsg.toString());
            return true;
        }else{
            _fileDB.setPKMsg(PKMsg);
            return true;
        }
    }
    
    public Boolean UpdatePKMsg(Player pl,Boolean PKMsg) {
        if(getUseMySql()) {
            Integer index = _mySqlDB.getPlayer(pl.getName()).getIndex();
            _mySqlDB.UpdateDetails(index,"PKMsg",PKMsg.toString());
            return true;
        }else{
            _fileDB.setPKMsg(PKMsg);
            return true;
        }
    }
    
    public Boolean getPKMsg(Player pl) {
        if(getUseMySql()) {
            Integer index = _mySqlDB.getPlayer(pl.getName()).getIndex();
            return Boolean.parseBoolean(_mySqlDB.GetDetails(index,"PKMsg"));
        }else{
            return _fileDB.getPKMsg();
        }
    }
    
    public Boolean SetAlertMsg(Player pl,Boolean AlertMsg) {
        if(getUseMySql()) {
            Integer index = _mySqlDB.getPlayer(pl.getName()).getIndex();
            _mySqlDB.SetDetails(index,"AlertMsg",AlertMsg.toString());
            return true;
        }else{
            _fileDB.setAlertMsg(AlertMsg);
            return true;
        }
    }
    
    public Boolean UpdateAlertMsg(Player pl,Boolean AlertMsg) {
        if(getUseMySql()) {
            Integer index = _mySqlDB.getPlayer(pl.getName()).getIndex();
            _mySqlDB.UpdateDetails(index,"AlertMsg",AlertMsg.toString());
            return true;
        }else{
            _fileDB.setAlertMsg(AlertMsg);
            return true;
        }
    }
    
    public Boolean getAlertMsg(Player pl) {
        if(getUseMySql()) {
            Integer index = _mySqlDB.getPlayer(pl.getName()).getIndex();
            return Boolean.parseBoolean(_mySqlDB.GetDetails(index,"AlertMsg"));
        }else{
            return _fileDB.getAlertMsg();
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
