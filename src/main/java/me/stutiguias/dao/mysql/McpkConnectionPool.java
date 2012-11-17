/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.mysql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Daniel
 */

public class McpkConnectionPool {
      
    private static McpkConnectionPool instance;
    
    public static McpkConnectionPool getInstance() {
            return instance;
    }
    
    private boolean ready = false;
    private static int poolsize = 10;
    private static List<McpkConnection> connections;
    private static long timeToLive = 300000;
    private final ConnectionReaper reaper;
    private String url;
    private String username;
    private String password;
    
    public McpkConnectionPool(String driverName, String url, String username, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Driver driver = (Driver) Class.forName(driverName).newInstance();
        McpkDriver jDriver = new McpkDriver(driver);
        DriverManager.registerDriver(jDriver);
        ready = true;
        this.url = url;
        this.username = username;
        this.password = password;
        connections = new ArrayList<McpkConnection>(poolsize);
        reaper = new ConnectionReaper();
        reaper.start();
        instance = this;
    }
    
    public boolean isReady(){
		return ready;
	}

    public synchronized McpkConnection getConnection() throws SQLException {
        if(!ready) return null;
		McpkConnection conn;
		for (int i = 0; i < connections.size(); i++) {
			conn = connections.get(i);
			if (conn.lease()) {
				if (conn.isValid())
					return conn;
				connections.remove(conn);
				conn.terminate();
			}
		}
		conn = new McpkConnection(DriverManager.getConnection(url, username, password));
		conn.lease();
		if (!conn.isValid()) {
			conn.terminate();
			throw new SQLException("Could not create new connection");
		}
		connections.add(conn);
		return conn;
    }
    
    public static synchronized void removeConn(Connection conn) {
		connections.remove((McpkConnection) conn);
    }
    
    public synchronized void closeConnection() {
            ready = false;
            for (McpkConnection conn : connections) {
                    conn.terminate();
            }
            connections.clear();
    }
    
    private synchronized void reapConnections() {
		if(!ready) return;
		final long stale = System.currentTimeMillis() - timeToLive;
		int count = 0;
		int i = 1;
		for (final McpkConnection conn : connections) {
			if (conn.inUse() && stale > conn.getLastUse() && !conn.isValid()) {
				connections.remove(conn);
				count++;
			}

			if (i > poolsize) {
				connections.remove(conn);
				count++;
				conn.terminate();
			}
			i++;
		}
	
    }

    private class ConnectionReaper extends Thread {
            @Override
            public void run() {
                    while (true) {
                            try {
                                    Thread.sleep(300000);
                            } catch (final InterruptedException e) {
                            }
                            reapConnections();
                    }
            }
    }
}