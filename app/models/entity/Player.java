package models.entity;

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import models.enums.AssetType;
import play.Logger;
import play.db.jpa.Model;

@Entity
public class Player extends Model {
    
    public String name;
    public int cash;
    public Date startTime;
    public int iconId;
    
    @ElementCollection
    private Map<AssetType, Integer> assets = new EnumMap<AssetType, Integer>(AssetType.class);
    
    @ElementCollection
    private Map<AssetType, Integer> assetTotals = new EnumMap<AssetType, Integer>(AssetType.class);
    
    public Player(String name, int cash, int iconId, Date startTime) {
        this.name = name;
        this.cash = cash;
        this.iconId = iconId;
        this.startTime = startTime;
        assetTotals.put(AssetType.OJ, 0);
        assetTotals.put(AssetType.PB, 0);
        assetTotals.put(AssetType.SB, 0);
    }

    public Map<AssetType, Integer> getAssets() {
        return assets;
    }

    public void put(AssetType assetType, Integer quantity) {
        assets.put(assetType, quantity);
    }
    
    public Map<AssetType, Integer> getAssetTotals() {
        return assetTotals;
    }
    
    public void addToTotals(AssetType assetType, Integer quantity) {
        assetTotals.put(assetType, assetTotals.get(assetType) + quantity);
    }
}
