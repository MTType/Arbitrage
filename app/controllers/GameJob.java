
package controllers;

import java.util.List;
import models.entity.Exchange;
import models.manager.ExchangeManager;
import models.manager.PlayerManager;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("5s")
public class GameJob extends Job{
    
    private static ExchangeManager exchangeManager = new ExchangeManager();
    private static PlayerManager playerManager = new PlayerManager();

    public void doJob() {
        if (Exchange.findAll().size() > 0) {
            Logger.info("The game has started!");
            List<Exchange> exchanges = Exchange.findAll();

            for (Exchange exchange: exchanges) {
                Logger.info("Looping through exchange: " + exchange.exchangeCode);
                exchangeManager.printRequests(exchange);
            }
            
            playerManager.printPlayer();
        } else {
            Logger.info("Game hasn't started yet");
        }
    }
}
