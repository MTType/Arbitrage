

package models.test;

import models.manager.PlayerManager;
import play.Logger;

public class TestH2 {
    
    public static void createPlayer() {
        PlayerManager player = new PlayerManager("Bobinamus", 1000);
        
        
        player.incCash(1000);
        float playerCash = player.getCash();
        Logger.info(Float.toString(playerCash));
    }
}
