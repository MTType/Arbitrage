
package controllers;

import java.util.List;
import models.entity.Exchange;
import models.manager.ExchangeManager;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("2s")
public class GameJob extends Job{
    
    private final ExchangeManager exchangeManager = new ExchangeManager();

    @Override
    public void doJob() {
        if (Exchange.findAll().size() > 0) {
            List<Exchange> exchanges = Exchange.findAll();

            for (Exchange exchange: exchanges) {
                exchangeManager.removeOldRequests(exchange);
            }
            
        } else {
            Logger.info("Game hasn't started yet");
        }
    }
}
