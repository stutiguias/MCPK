/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import java.sql.Date;
import java.util.Calendar;

/**
 *
 * @author Daniel
 */
public class Comuns {
        
    public Date addTime(String Time)
    {
        Calendar cal = Calendar.getInstance();
        if(Time.contains("m")) {
            cal.add(Calendar.MINUTE, Integer.parseInt(Time.replace("m","")) );    
        }
        java.sql.Date dataSql = new java.sql.Date(cal.getTime().getTime()); 
        return dataSql;
    }
    
    public Date now() {
        java.util.Date dataUtil = new java.util.Date();  
        java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());  
        return dataSql;
    }
}
