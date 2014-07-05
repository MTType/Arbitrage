
package models.manager;

import java.util.Date;
import java.util.Map;
import models.entity.Player;
import models.entity.Request;
import models.enums.AssetType;
import models.exception.ArbitrageException;
import models.response.PlayerJSON;
import play.Logger;
import play.db.jpa.Transactional;

public class PlayerManager {
    
    Player player;
    
    @Transactional
    public void createPlayer(String name, int startingCash) {
        player = new Player(name, startingCash); 
        player.put(AssetType.PB, 0);
        player.put(AssetType.OJ, 0);
        player.put(AssetType.SB, 0);
        player.save();
    }
    
    @Transactional
    public void setCash(float cashIn) {
        player = (Player) Player.findAll().get(0);
        player.cash = cashIn;
        player.save();
    }
    
    @Transactional
    public void incCash(float increment) {
        player = (Player) Player.findAll().get(0);
        player.cash += increment;
        player.save();
    }
    
    @Transactional
    public int getAssetAmount(AssetType asset) {
        player = (Player) Player.findAll().get(0);
        return player.getAssets().get(asset);
    }
    
    @Transactional
    public void setAssetAmount(AssetType asset, int newAmount) {
        player = (Player) Player.findAll().get(0);
        player.getAssets().put(asset, newAmount);
        player.save();
    }

    @Transactional
    private void incAssetAmount(AssetType asset, int inc) {
        Logger.info("changing amount of " + asset.name() + " by this amount: " + inc);
        player = (Player) Player.findAll().get(0);
        player.getAssets().put(asset, player.getAssets().get(asset) + inc);
        player.save();
    }

    @Transactional
    public boolean acceptOffer(Request request) throws ArbitrageException{
        player = (Player) Player.findAll().get(0);
        Logger.info("Trying to accept a request of " + request.quantity + " of this type of asset: " + request.assetType);
        if (player.getAssets().get(request.assetType) + request.quantity < 0 ) {
            //popup box tells user they do not have enough of that asset to perform that transaction
            throw new ArbitrageException("You do not have enough of that asset to perform that transaction");
        } else if (player.cash - request.pricePerUnit*request.quantity < 0) {
            //popup box tells user they do not have enough cash to perform that transaction
            throw new ArbitrageException("You do not have enough cash to perform that transaction");
        } else {
            incAssetAmount(request.assetType, request.quantity);
            player.cash -= request.pricePerUnit*request.quantity;
            player.save();
            return true;
        }
    }
    
    public void printPlayer() {
        player = (Player) Player.findAll().get(0);
        Logger.info("Player info:");
        Logger.info("   name: " + player.name);
        Logger.info("   cash: " + player.cash);
        Logger.info("   asset list size: " + player.getAssets().size()); 
        for (Map.Entry<AssetType, Integer> entry : player.getAssets().entrySet()) {
            Logger.info("   " + entry.getKey().name() + ": " + entry.getValue()); 
        }
    }
    
    public PlayerJSON getPlayerJSON(){       
        return new PlayerJSON(player.name, player.getAssets().get(AssetType.PB), player.getAssets().get(AssetType.OJ), player.getAssets().get(AssetType.SB), player.cash, player.startTime);
    }

}
