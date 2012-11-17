/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.mysql;

import java.sql.*;
import java.util.Properties;

/**
 *
 * @author Daniel
 */
public class McpkDriver implements Driver {
    
    private Driver driver;
     
    public McpkDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return driver.connect(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return driver.acceptsURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return driver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }

}
