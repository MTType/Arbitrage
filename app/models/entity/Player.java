package models.entity;

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import models.enums.AssetType;
import play.db.jpa.Model;

@Entity
public class Player extends Model {
    
    public String name;
    public float cash;
    public Date startTime;
    
    @ElementCollection
    private Map<AssetType, Integer> assets = new EnumMap<AssetType, Integer>(AssetType.class);
    
    public Player(String name, float cash, Date startTime) {
        this.name = name;
        this.cash = cash;
        this.startTime = startTime;
    }

    public Map<AssetType, Integer> getAssets() {
        return assets;
    }

    public void put(AssetType assetType, Integer quantity) {
        assets.put(assetType, quantity);
    }
}
