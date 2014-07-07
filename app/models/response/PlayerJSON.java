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
    private int porkBelliesTotal;
    private int frozenOrangeJuiceTotal;
    private int soyBeansTotal;

    public PlayerJSON(String name, int porkBellies, int frozenOrangeJuice, int soyBeans, float cash, Date startTime, int iconId, int porkBelliesTotal, int frozenOrangeJuiceTotal, int soyBeansTotal) {
        this.name = name;
        this.porkBellies = porkBellies;
        this.frozenOrangeJuice = frozenOrangeJuice;
        this.soyBeans = soyBeans;
        this.cash = cash;
        this.startTime = startTime;
        this.iconId = iconId;
        this.porkBelliesTotal = porkBelliesTotal;
        this.frozenOrangeJuiceTotal = frozenOrangeJuiceTotal;
        this.soyBeansTotal = soyBeansTotal;
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

    public int getPorkBelliesTotal() {
        return porkBelliesTotal;
    }

    public void setPorkBelliesTotal(int porkBelliesTotal) {
        this.porkBelliesTotal = porkBelliesTotal;
    }

    public int getFrozenOrangeJuiceTotal() {
        return frozenOrangeJuiceTotal;
    }

    public void setFrozenOrangeJuiceTotal(int frozenOrangeJuiceTotal) {
        this.frozenOrangeJuiceTotal = frozenOrangeJuiceTotal;
    }

    public int getSoyBeansTotal() {
        return soyBeansTotal;
    }

    public void setSoyBeansTotal(int soyBeansTotal) {
        this.soyBeansTotal = soyBeansTotal;
    }
    
}
