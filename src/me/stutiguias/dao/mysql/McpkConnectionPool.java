/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.mysql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Daniel
 */

public class McpkConnectionPool {
    
    private McpkConnection connection;
    private String url;
    private String username;
    private String password;
    
    public McpkConnectionPool(String driverName, String url, String username, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Driver driver = (Driver) Class.forName(driverName).newInstance();
        McpkDriver _McpkDriver = new McpkDriver(driver);
        DriverManager.registerDriver(_McpkDriver);
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public synchronized McpkConnection getConnection() throws SQLException {
        if (connection != null && (connection.isClosed() || !connection.isValid(1))) {
            try {
                connection.closeConnection();
            } catch (SQLException e) {}
            connection = null;
        }
        
        if (connection == null) {
            Connection conn = DriverManager.getConnection(url, username, password);
            connection = new McpkConnection(conn);
        }
        
        return connection;
    }
    
    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}