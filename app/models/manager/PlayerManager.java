
package models.manager;

import javax.persistence.EntityManager;
import models.BuyRequest;
import models.Request;
import models.SellRequest;
import models.entity.Player;
import models.exception.PlayerException;
import play.db.DB;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public class PlayerManager {
    
    private Player player;
    
    public PlayerManager(String name, int startingCash) {
        DB.getConnection();
        EntityManager em = JPA.em();
        this.player = new Player();
        player.name = name;
        player.cash = startingCash;
        em.persist(player);        
    }

    @Transactional
    public float getCash() {
        player = Player.findById(player.id);
        return player.cash;
    }
    
    @Transactional
    public void setCash(float cashIn) {
        player = Player.findById(player.id);
        player.cash = cashIn;
    }
    
    @Transactional
    public void incCash(float increment) {
        player = Player.findById(player.id);
        player.cash += increment;
    }
    
    @Transactional
    public int getAssetAmount(String asset) {
        player = Player.findById(player.id);
        if ("Pork Bellies".equals(asset)) {
            return player.pb;
        } else if ("Frozen Orange Juice Concentrate".equals(asset)) {
            return player.oj;
        } else if ("Soybeans".equals(asset)) {
            return player.sb;
        } else {
            return 0;
        }
    }
    
    @Transactional
    public void setAssetAmount(String asset, int newAmount) {
        player = Player.findById(player.id);
        if ("Pork Bellies".equals(asset)) {
            player.pb = newAmount;
        } else if ("Frozen Orange Juice Concentrate".equals(asset)) {
            player.oj = newAmount;
        } else if ("Soybeans".equals(asset)) {
            player.sb = newAmount;
        }
    }

    @Transactional
    private void incAssetAmount(String asset, int inc) {
        player = Player.findById(player.id);
        if ("Pork Bellies".equals(asset)) {
            player.pb += inc;
        } else if ("Frozen Orange Juice Concentrate".equals(asset)) {
            player.oj += inc;
        } else if ("Soybeans".equals(asset)) {
            player.sb += inc;
        }
    }

    @Transactional
    public boolean acceptOffer(Request request) throws PlayerException{
        player = Player.findById(player.id);
        float cost = request.getUnitPrice()*request.getAssetAmount();
        String assetType = request.getAssetType();
        
        if (request instanceof BuyRequest) {
            if (getAssetAmount(assetType) - request.getAssetAmount() < 0 ) {
                //popup box tells user they do not have enough of that asset to perform that transaction
                throw new PlayerException("You do not have enough of that asset to perform that transaction");
            } else {
                incAssetAmount(assetType, -request.getAssetAmount());
                player.cash -= cost;
                return true;
            }
        } else if (request instanceof SellRequest) {           
            if ((player.cash - cost) < 0) {
                //popup box tells user they do not have enough cash to perform that transaction
                throw new PlayerException("You do not have enough cash to perform that transaction");
            } else {
                incAssetAmount(assetType, request.getAssetAmount());
                player.cash += cost;
                return true;
            } 
        } 
        return false;
    }

}
