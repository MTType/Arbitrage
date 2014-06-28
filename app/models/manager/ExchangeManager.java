
package models.manager;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import models.entity.Exchange;
import models.entity.Request;
import models.enums.AssetType;
import models.enums.ExchangeCode;
import models.enums.RequestType;
import play.Logger;
import play.db.jpa.Transactional;

public class ExchangeManager {
    
    private final Random rng = new Random();
    private static final DecimalFormat tdp = new DecimalFormat("###.##");
    private static final DecimalFormat zdp = new DecimalFormat("###");
    
    @Transactional
    public void createExchange(ExchangeCode exchangeCode, int numberOfRequests, float standardDeviation) {
        Exchange exchange = new Exchange(exchangeCode, 1.0f, 1.0f, standardDeviation).save(); 
        
        for(int i = 0; i<numberOfRequests; i++){
            addRequest(exchange);    
        }
        exchange.save();
    }
    
    @Transactional
    private void addRequest(Exchange exchange) {
        float targetPrice;
        int sORb = rng.nextInt(2);

        int quantity = ((rng.nextInt(4)+1));
        int assetTypeNumber = rng.nextInt(3); 
        Request request;
        if (sORb == 0) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation) + exchange.meanSellPrice);
            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.SELL, (quantity*40000), (int)(targetPrice*1.28f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.SELL, (quantity*15000), (int)(targetPrice*1.57f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.SELL, (quantity*5000), (int)(targetPrice*14.05f), new Date()).save();
                    exchange.requests.add(request);
            }
        }
        if (sORb == 1) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation)+ exchange.meanBuyPrice);

            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.BUY, (quantity*40000), (int)(targetPrice*1.28f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.BUY, (quantity*15000), (int)(targetPrice*1.57f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.BUY, (quantity*5000), (int)(targetPrice*14.05f), new Date()).save();
                    exchange.requests.add(request);
            }
        }
        exchange.save();
    }
    
    @Transactional
    public void removeRequest(Exchange exchange, int loc){
        Request removedRequest = exchange.requests.get(loc);
        removedRequest.delete();
        exchange.save();
        
        float targetPrice;
        Request request;
        int quantity = ((rng.nextInt(4)+1)); 
        int assetTypeNumber = rng.nextInt(3); 
        
        int sORb = rng.nextInt(2);
        if (sORb==0) {    
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation) + exchange.meanSellPrice + (exchange.sellVolume*exchange.standardDeviation*0.03));
            if (targetPrice<0) {
                targetPrice = 0;
            }
            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.SELL, (quantity*40000), (int)(targetPrice*1.28f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.SELL, (quantity*15000), (int)(targetPrice*1.57f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.SELL, (quantity*5000), (int)(targetPrice*14.05f), new Date()).save();
                    exchange.requests.add(request);
            }
        } else if(sORb==1) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation) + exchange.meanBuyPrice - (exchange.buyVolume*exchange.standardDeviation*0.03));
            if (targetPrice<0) {
                targetPrice = 0;
            }
            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.BUY, (quantity*40000), (int)(targetPrice*1.28f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.BUY, (quantity*15000), (int)(targetPrice*1.57f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.BUY, (quantity*5000), (int)(targetPrice*14.05f), new Date()).save();
                    exchange.requests.add(request);
            }
        }
        
        
        addRequest(exchange);
        exchange.save();
        
        printRequest(exchange, exchange.requests.size()-1);
    }
    
    @Transactional
    public void printRequests(){
        for (ExchangeCode exchangeCode: ExchangeCode.values()) {
            List<Request> exchangeRequests = Request.find("byExchange", Exchange.find("byExchangeCode", exchangeCode).fetch()).fetch();
            System.out.println("Exchange: " + exchangeCode.name() + ": found " + exchangeRequests.size() + " requests");
            for (Request request: exchangeRequests) {
                Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at £" + tdp.format(request.pricePerUnit) + " totalling £" + zdp.format(request.quantity * request.pricePerUnit));
            }
        }
    }
    
    @Transactional
    public void printRequests(Exchange exchange){
        List<Request> exchangeRequests = Request.find("byExchange", exchange).fetch();
        System.out.println("Exchange: " + exchange.exchangeCode + ": found " + exchangeRequests.size() + " requests");
        for (Request request: exchangeRequests) {
            Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at £" + tdp.format(request.pricePerUnit) + " totalling £" + zdp.format(request.quantity * request.pricePerUnit));
        }
    }
    
    @Transactional
    public void printRequest(Exchange exchange, int loc){
        List<Request> exchangeRequests = Request.find("byExchange", exchange).fetch();
        System.out.println("Exchange: " + exchange.exchangeCode + ": found " + exchangeRequests.size() + " requests");
        if (loc < exchangeRequests.size() && loc > 0) {
            Request request = exchangeRequests.get(loc);
            Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at £" + tdp.format(request.pricePerUnit) + " totalling £" + zdp.format(request.quantity * request.pricePerUnit));
        }
    }
    
    @Transactional
    public void addAndShift(Exchange exchange){
        Request oldestRequest = exchange.requests.get(0);
        for (Request request: exchange.requests) {
            oldestRequest = oldestRequest.timestamp.before(request.timestamp) ? oldestRequest : request;
        }
        exchange.requests.remove(oldestRequest);
        exchange.save();
        
        addRequest(exchange);
        
        if(exchange.sellVolume > 0){
            exchange.sellVolume--;
        }
        if(exchange.buyVolume > 0){
            exchange.buyVolume--;
        }
        exchange.save();
        
        printRequest(exchange, exchange.requests.size()-1);
    }
}
