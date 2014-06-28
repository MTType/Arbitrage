package models.entity;

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
    
    @ElementCollection
    private Map<AssetType, Integer> assets = new EnumMap<AssetType, Integer>(AssetType.class);
    
    public Player(String name, float cash) {
        this.name = name;
        this.cash = cash;
    }

    public Map<AssetType, Integer> getAssets() {
        return assets;
    }

    public void put(AssetType assetType, Integer quantity) {
        assets.put(assetType, quantity);
    }
}
