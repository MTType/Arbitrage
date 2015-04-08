
package controllers;

import java.util.List;
import models.entity.Exchange;
import models.manager.ExchangeManager;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("1s")
public class GameJob extends Job{
    
    private final ExchangeManager exchangeManager = new ExchangeManager();

    @Override
    public void doJob() {
        List<Exchange> exchanges = Exchange.findAll();

        if (exchanges == null || exchanges.isEmpty()) {
            Logger.debug("Game not initialised yet... ");
        } else {
            for (Exchange exchange: exchanges) {
                exchangeManager.removeOldRequests(exchange);
            }
            EventHandler.instance.event.publish("refresh");
        }
    }
}
