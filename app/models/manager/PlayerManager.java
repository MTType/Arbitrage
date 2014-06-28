
package models.manager;

import java.util.Map;
import models.entity.Player;
import models.entity.Request;
import models.enums.AssetType;
import models.exception.PlayerException;
import play.Logger;
import play.db.jpa.Transactional;

public class PlayerManager {
    
    private Player player;
    
    public void createPlayer(String name, int startingCash) {
        this.player = new Player(name, startingCash).save();     
    }
    
    @Transactional
    public void setCash(float cashIn) {
        player.cash = cashIn;
        player.save();
    }
    
    @Transactional
    public void incCash(float increment) {
        player.cash += increment;
        player.save();
    }
    
    @Transactional
    public int getAssetAmount(AssetType asset) {
        return player.getAssets().get(asset);
    }
    
    @Transactional
    public void setAssetAmount(AssetType asset, int newAmount) {
        player.getAssets().put(asset, newAmount);
        player.save();
    }

    @Transactional
    private void incAssetAmount(AssetType asset, int inc) {
        Logger.info("changing amount of ");
        player.getAssets().put(asset, player.getAssets().get(asset) + inc);
        player.save();
    }

    @Transactional
    public boolean acceptOffer(Request request) throws PlayerException{
        Logger.info("Trying to accept a request of " + request.quantity + " of this type of asset: " + request.assetType);
        if (player.getAssets().get(request.assetType) + request.quantity < 0 ) {
            //popup box tells user they do not have enough of that asset to perform that transaction
            throw new PlayerException("You do not have enough of that asset to perform that transaction");
        } else if (player.cash - request.pricePerUnit*request.quantity < 0) {
            //popup box tells user they do not have enough cash to perform that transaction
            throw new PlayerException("You do not have enough cash to perform that transaction");
        } else {
            incAssetAmount(request.assetType, request.quantity);
            player.cash -= request.pricePerUnit*request.quantity;
            player.save();
            return true;
        }
    }
    
    public void printPlayer() {
        Logger.info("Player info:");
        Logger.info("   name: " + player.name);
        Logger.info("   cash: " + player.cash);
        for (Map.Entry<AssetType, Integer> entry : player.getAssets().entrySet()) {
            Logger.info("   " + entry.getKey().name() + ": " + entry.getValue()); 
        }
    }

}
