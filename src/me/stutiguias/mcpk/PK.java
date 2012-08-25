package me.stutiguias.mcpk;

import java.util.Date;

/**
 *
 * @author Stutiguias
 */
public class PK {
    
    private int index;
    private String name;
    private long time;
    private int kills;
    private Date newBie;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return the kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * @param kills the kills to set
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKills(int kill){
        this.kills = this.kills + kill;
    }
    
    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the newBie
     */
    public Date getNewBie() {
        return newBie;
    }

    /**
     * @param newBie the newBie to set
     */
    public void setNewBie(Date newBie) {
        this.newBie = newBie;
    }
    
}
