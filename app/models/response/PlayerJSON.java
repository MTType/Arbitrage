package models.response;

import java.util.Date;

public class PlayerJSON {
    
    private String name;    
    private int porkBellies;
    private int frozenOrangeJuice;
    private int soyBeans;
    private float cash;
    private Date startTime;
    private int iconId;

    public PlayerJSON(String name, int porkBellies, int frozenOrangeJuice, int soyBeans, float cash, Date startTime, int iconId) {
        this.name = name;
        this.porkBellies = porkBellies;
        this.frozenOrangeJuice = frozenOrangeJuice;
        this.soyBeans = soyBeans;
        this.cash = cash;
        this.startTime = startTime;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public int getPorkBellies() {
        return porkBellies;
    }

    public int getFrozenOrangeJuice() {
        return frozenOrangeJuice;
    }

    public int getSoyBeans() {
        return soyBeans;
    }

    public float getCash() {
        return cash;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getIconId() {
        return iconId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPorkBellies(int porkBellies) {
        this.porkBellies = porkBellies;
    }

    public void setFrozenOrangeJuice(int frozenOrangeJuice) {
        this.frozenOrangeJuice = frozenOrangeJuice;
    }

    public void setSoyBeans(int soyBeans) {
        this.soyBeans = soyBeans;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
    
    
    
}
