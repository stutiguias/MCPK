/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.dao.type;

import java.sql.*;
import java.util.logging.Level;
import me.stutiguias.dao.mysql.McpkConnection;
import me.stutiguias.dao.mysql.McpkConnectionPool;
import me.stutiguias.mcpk.Util;
import me.stutiguias.mcpk.Mcpk;
import me.stutiguias.mcpk.MCPlayer;

/**
 *
 * @author Daniel
 */
public class MySqlDB {
            
    private McpkConnectionPool pool;
    public String dbHost;
    public String dbPort;
    public String dbDatabase;
    
    public MySqlDB(String dbHost, String dbUser, String dbPass, String dbPort, String dbDatabase) {
        try {
             this.dbHost = dbHost;
             this.dbDatabase = dbDatabase;
             this.dbPort = dbPort;
             pool = new McpkConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://"+ dbHost +":"+ dbPort +"/"+ dbDatabase, dbUser, dbPass);
        }catch(InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) { }
    }
    
    public McpkConnection GetConnection() throws SQLException {
         try {
            return pool.getConnection();
        } catch (SQLException e) {
          //  core.getPluginLogger().severe("Unable to connect to the database: "+e.getMessage());
            return null;
        } 
    }
    
    private boolean tableExists(String tableName) {
		boolean exists = false;
                McpkConnection conn = null;
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
			Mcpk.logger.log(Level.WARNING,Mcpk.logPrefix + "Unable to check if table exists: {0}", tableName);
			Mcpk.logger.warning(e.getMessage());
		} finally {
			closeResources(conn, st, rs);
		}
		return exists;
    }
    
    private void executeRawSQL(String sql) {
            McpkConnection conn = null;
            Statement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.createStatement();
                    st.executeUpdate(sql);
            } catch (SQLException e) {
                    Mcpk.logger.log(Level.WARNING,Mcpk.logPrefix + "Exception executing raw SQL {0}", sql);
                    Mcpk.logger.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
    }
    
    private void closeResources(McpkConnection conn, Statement st, ResultSet rs) {
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
                   conn.close();
            }
    }
    
    public void InitTables() {
        if (!tableExists("MCPlayer")) {
            Mcpk.logger.info(Mcpk.logPrefix + "Creating table MCPK_player");
            executeRawSQL("CREATE TABLE MCPlayer (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), pass VARCHAR(255), pkCount INTEGER, newbieCount TIMESTAMP);");
        }
        if (!tableExists("MCSettings")) {
            Mcpk.logger.info(Mcpk.logPrefix + "Creating table MCSettings");
            executeRawSQL("CREATE TABLE MCSettings (id INTEGER PRIMARY KEY AUTO_INCREMENT, playerid INTEGER, playerdetail VARCHAR(255), playerval VARCHAR(255), since TIMESTAMP);");
        }
    }
    
    public void createPlayer(String player, String pass, int pkCount, Timestamp newbieCount) {
            McpkConnection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("INSERT INTO MCPlayer (name, pass, pkCount, newbieCount) VALUES (?, ?, ?, ?)");
                    st.setString(1, player);
                    st.setString(2, pass);
                    st.setDouble(3, pkCount);
                    st.setTimestamp(4, newbieCount);
                    st.executeUpdate();
            } catch (SQLException e) {
                    Mcpk.logger.warning(Mcpk.logPrefix + "Unable to update player permissions in DB");
                    Mcpk.logger.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
    }
        
    public MCPlayer getPlayer(String player) {
            MCPlayer _Player = null;

            McpkConnection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("SELECT id,name, pass, pkCount, newbieCount FROM MCPlayer WHERE name = ?");
                    st.setString(1, player);
                    rs = st.executeQuery();
                    while (rs.next()) {
                            _Player = new MCPlayer();
                            _Player.setIndex(rs.getInt("id"));
                            _Player.setName(rs.getString("name"));
                            _Player.setKills(rs.getInt("pkCount"));
                            _Player.setNewBieProtectUntil(rs.getTimestamp("newbieCount"));
                    }
            } catch (SQLException e) {
                    Mcpk.logger.log(Level.WARNING,Mcpk.logPrefix + "Unable to get player {0}", player);
                    Mcpk.logger.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
            return _Player;
    }
    
    public boolean UpdateKill(String player,Integer kill) {
        	McpkConnection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
                        conn = GetConnection();
			st = conn.prepareStatement("UPDATE MCPlayer SET pkCount = ? WHERE name = ?");
			st.setInt(1, kill);
			st.setString(2, player);
			st.executeUpdate();
                        return true;
		} catch (SQLException e) {
			Mcpk.logger.log(Level.WARNING, "{0} Unable to update item quantity in DB", Mcpk.logPrefix);
			Mcpk.logger.warning(e.getMessage());
                        return false;
		} finally {
			closeResources(conn, st, rs);
		}
    }
    
    public String GetDetails(Integer PlayerId,String Detail) {
            String val = null;
            McpkConnection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("SELECT playerval FROM MCSettings WHERE playerid = ? and playerdetail = ?");
                    st.setInt(1, PlayerId);
                    st.setString(2, Detail);
                    rs = st.executeQuery();
                    while (rs.next()) {
                            val = rs.getString("playerval");
                    }
            } catch (SQLException e) {
                    Mcpk.logger.log(Level.WARNING,Mcpk.logPrefix + "Unable to get detail {0}{1}", new Object[] { Detail,PlayerId });
                    Mcpk.logger.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
            return val;
    }
    
    public void SetDetails(Integer PlayerId,String Detail,String Val) {
            McpkConnection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("INSERT INTO MCSettings (playerid, playerdetail, playerval, since) VALUES (?, ?, ?, ?)");
                    st.setInt(1, PlayerId);
                    st.setString(2, Detail);
                    st.setString(3, Val);
                    Timestamp since = new Timestamp(new Util().now().getTime());
                    st.setTimestamp(4,since);
                    st.executeUpdate();
            } catch (SQLException e) {
                    Mcpk.logger.warning(Mcpk.logPrefix + "Unable to Set Detail");
                    Mcpk.logger.warning(e.getMessage());
            } finally {
                    closeResources(conn, st, rs);
            }
    }
    
    public Boolean UpdateDetails(Integer PlayerId,String Detail,String Val) {
            McpkConnection conn = null;
            PreparedStatement st = null;
            ResultSet rs = null;

            try {
                    conn = GetConnection();
                    st = conn.prepareStatement("UPDATE MCSettings SET playerval = ? WHERE playerid = ? and playerdetail = ?");
                    st.setString(1, Val);
                    st.setInt(2, PlayerId);
                    st.setString(3, Detail);
                    st.executeUpdate();
                    return true;
            } catch (SQLException e) {
                    Mcpk.logger.log(Level.WARNING, "{0} Unable to update details", Mcpk.logPrefix);
                    Mcpk.logger.warning(e.getMessage());
                    return false;
            } finally {
                    closeResources(conn, st, rs);
            }
    }
}
