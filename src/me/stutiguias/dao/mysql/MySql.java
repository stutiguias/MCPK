/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.mysql;

import java.sql.*;
import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.mcpk.PK;

/**
 *
 * @author Daniel
 */
public class MySql {
    		
    public String dbHost;
    public String dbPort;
    public String dbDatabase;
    
    public MySql(String dbHost, String dbUser, String dbPass, String dbPort, String dbDatabase) {
        try {
             this.dbHost = dbHost;
             this.dbDatabase = dbDatabase;
             this.dbPort = dbPort;
             new JDCConnectionDriver("com.mysql.jdbc.Driver", "jdbc:mysql://"+ dbHost +":"+ dbPort +"/"+ dbDatabase, dbUser, dbPass);
        }catch(Exception e) { }
    }
    
    public Connection GetConnection() throws SQLException {
         return DriverManager.getConnection("jdbc:mysql://"+ dbHost +":"+ dbPort +"/" + dbDatabase);
    }
    
    private boolean tableExists(String tableName) {
		boolean exists = false;
                Connection conn = null;
                PreparedStatement st = null;
                ResultSet rs = null;
		try {
                        conn = GetConnection();
			st = conn.prepareStatement("SHOW TABLES LIKE ?");
			st.setString(1, tableName);
			rs = st.executeQuery();
			while (rs.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Mcpk.log.warning(Mcpk.logPrefix + "Unable to check if table exists: " + tableName);
			Mcpk.log.warning(e.getMessage());
		} finally {
			closeResources(conn, st, rs);
		}
		return exists;
    }
    
    private void executeRawSQL(String sql) {
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.createStatement();
                    st.executeUpdate(sql);
            } catch (SQLException e) {
                    Mcpk.log.warning(Mcpk.logPrefix + "Exception executing raw SQL" + sql);
                    Mcpk.log.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
    }
    
    private void closeResources(Connection conn, Statement st, ResultSet rs) {
            if (null != rs) {
                    try {
                            rs.close();
                    } catch (SQLException e) {
                    }
            }
            if (null != st) {
                    try {
                            st.close();
                    } catch (SQLException e) {
                    }
            }
            if (null != conn) {
                    try {
                            conn.close();
                    } catch (SQLException e) {
                    }
            }
    }
    
    public void InitTables() {
        if (!tableExists("MCPK_player")) {
			Mcpk.log.info(Mcpk.logPrefix + "Creating table MCPK_player");
			executeRawSQL("CREATE TABLE MCPK_player (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), pass VARCHAR(255), pkCount INTEGER, newbieCount DATETIME);");
		}
    }
    
    public void createPlayer(String player, String pass, int pkCount, Date newbieCount) {
            Connection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("INSERT INTO MCPK_player (name, pass, pkCount, newbieCount) VALUES (?, ?, ?, ?)");
                    st.setString(1, player);
                    st.setString(2, pass);
                    st.setDouble(3, pkCount);
                    st.setDate(4, newbieCount);
                    st.executeUpdate();
            } catch (SQLException e) {
                    Mcpk.log.warning(Mcpk.logPrefix + "Unable to update player permissions in DB");
                    Mcpk.log.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
    }
        
    public PK getPlayer(String player) {
            PK _Player = null;

            Connection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("SELECT name, pass, pkCount, newbieCount FROM MCPK_player WHERE name = ?");
                    st.setString(1, player);
                    rs = st.executeQuery();
                    while (rs.next()) {
                            _Player = new PK();
                            _Player.setName(rs.getString("name"));
                            _Player.setKills(rs.getInt("pkCount"));
                            _Player.setNewBie(rs.getDate("newbieCount"));
                    }
            } catch (SQLException e) {
                    Mcpk.log.warning(Mcpk.logPrefix + "Unable to get player " + player);
                    Mcpk.log.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
            return _Player;
    }
}
