package models.response;

import java.util.Date;

public class PlayerJSON {
    
    String name;
    private int porkBellies;
    private int frozenOrangeJuice;
    private int soyBeans;
    private float cash;
    private Date startTime;

    public PlayerJSON(String name, int porkBellies, int frozenOrangeJuice, int soyBeans, float cash, Date startTime) {
        this.name = name;
        this.porkBellies = porkBellies;
        this.frozenOrangeJuice = frozenOrangeJuice;
        this.soyBeans = soyBeans;
        this.cash = cash;
        this.startTime = startTime;
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
    
    
    
}
