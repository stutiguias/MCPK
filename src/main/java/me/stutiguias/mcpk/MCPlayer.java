package me.stutiguias.mcpk;

import java.util.Date;

/**
 *
 * @author Stutiguias
 */
public class MCPlayer {
    
    private int index;
    private String name;
    private long time;
    private int kills;
    private Date newBie;
    private String[] PkOldGroups;
    private Boolean PKMsg;
    private Boolean AlertMsg;
    private Boolean isPK;
    
    public MCPlayer() {
        
    }
    
    public MCPlayer(String name,Date newBie) {
        this.name = name;
        time = 0;
        kills = 0;
        this.newBie = newBie;
        isPK = false;
    }
    
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

    /**
     * @return the PkOldGroups
     */
    public String[] getPkOldGroups() {
        return PkOldGroups;
    }

    /**
     * @param PkOldGroups the PkOldGroups to set
     */
    public void setPkOldGroups(String[] PkOldGroups) {
        this.PkOldGroups = PkOldGroups;
    }

    /**
     * @return the PKMsg
     */
    public Boolean getPKMsg() {
        return PKMsg;
    }

    /**
     * @param PKMsg the PKMsg to set
     */
    public void setPKMsg(Boolean PKMsg) {
        this.PKMsg = PKMsg;
    }

    /**
     * @return the AlertMsg
     */
    public Boolean getAlertMsg() {
        return AlertMsg;
    }

    /**
     * @param AlertMsg the AlertMsg to set
     */
    public void setAlertMsg(Boolean AlertMsg) {
        this.AlertMsg = AlertMsg;
    }

    /**
     * @return the isPK
     */
    public Boolean getIsPK() {
        return isPK;
    }

    /**
     * @param isPK the isPK to set
     */
    public void setIsPK(Boolean isPK) {
        this.isPK = isPK;
    }
    
}
