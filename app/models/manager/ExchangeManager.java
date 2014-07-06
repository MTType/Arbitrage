
package models.manager;

import controllers.EventHandler;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import models.entity.Exchange;
import models.entity.Request;
import models.enums.AssetType;
import models.enums.ExchangeCode;
import models.enums.RequestType;
import models.response.RequestJSON;
import play.Logger;
import play.db.jpa.Transactional;

public class ExchangeManager {
    
    private final Random rng = new Random();
    private static final DecimalFormat tdp = new DecimalFormat("###.##");
    private static final DecimalFormat zdp = new DecimalFormat("###");
    private static final int REQUEST_EXPIRE_TIME = 5;
    
    @Transactional
    public void createExchange(ExchangeCode exchangeCode, int numberOfRequests, float standardDeviation) {
        Exchange exchange = new Exchange(exchangeCode, 1.0f, 1.0f, standardDeviation).save(); 
        
        for(int i = 0; i<numberOfRequests; i++){
            addRequest(exchange);    
        }
        exchange.save();
        
        EventHandler.instance.event.publish("refresh");
    }
    
    private Exchange getExchange(ExchangeCode exchangeCode) {
        return Exchange.find("byExchangeCode", exchangeCode).first();
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
                    request = new Request(exchange, AssetType.PB, RequestType.SELL, (quantity*200), (int)(targetPrice*6.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.SELL, (quantity*400), (int)(targetPrice*12.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.SELL, (quantity*800), (int)(targetPrice*24.0f), new Date()).save();
                    exchange.requests.add(request);
            }
        }
        if (sORb == 1) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation)+ exchange.meanBuyPrice);

            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.BUY, (quantity*200), (int)(targetPrice*6.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.BUY, (quantity*400), (int)(targetPrice*12.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.BUY, (quantity*800), (int)(targetPrice*24.0f), new Date()).save();
                    exchange.requests.add(request);
            }
        }
        exchange.save();
    }
    
    @Transactional
    public void removeOldRequests(Exchange exchange) {
        List<Request> exchangeRequests = Request.find("byExchange", exchange).fetch();
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.SECOND, -REQUEST_EXPIRE_TIME);
        for (Request request: exchangeRequests) {
            if (request.timestamp.before(expireTime.getTime())) {
                //Logger.info("removing expired request with timestamp: " + request.timestamp);
                removeRequest(exchange, request.id);
            }
        }

        EventHandler.instance.event.publish("refresh");
    }
    
    @Transactional
    public void removeRequest(ExchangeCode exchangeCode, long id){
        removeRequest(getExchange(exchangeCode), id);
    }
    
    @Transactional
    public void removeRequest(Exchange exchange, long id){
        Request requestToRemove = null;
        for (Request request: exchange.requests) { 
            if (request.id == id) {
                requestToRemove = request;
            }
        }
        if (requestToRemove == null) {
            return;
        }
        
        exchange.requests.remove(requestToRemove);
        exchange.save();
        requestToRemove.delete();
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
                    request = new Request(exchange, AssetType.PB, RequestType.SELL, (quantity*200), (int)(targetPrice*6.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.SELL, (quantity*400), (int)(targetPrice*12.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.SELL, (quantity*800), (int)(targetPrice*24.0f), new Date()).save();
                    exchange.requests.add(request);
            }
        } else if(sORb==1) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation) + exchange.meanBuyPrice - (exchange.buyVolume*exchange.standardDeviation*0.03));
            if (targetPrice<0) {
                targetPrice = 0;
            }
            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.BUY, (quantity*200), (int)(targetPrice*6.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.BUY, (quantity*400), (int)(targetPrice*12.0f), new Date()).save();
                    exchange.requests.add(request);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.BUY, (quantity*800), (int)(targetPrice*24.0f), new Date()).save();
                    exchange.requests.add(request);
            }
        }
        
        exchange.save();  
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
    public void printRequest(long requestId){
        Request request = Request.findById(requestId);
        Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at £" + tdp.format(request.pricePerUnit) + " totalling £" + zdp.format(request.quantity * request.pricePerUnit));
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
    
    public List<RequestJSON> getRequestJSONs(ExchangeCode exchangeCode) {
        List<Request> requests = Request.find("select r from Request r where r.exchange = ? order by r.timestamp asc", getExchange(exchangeCode)).fetch();
        List<RequestJSON> requestJSONS = new ArrayList<RequestJSON>();
        for (Request request: requests) {
            requestJSONS.add(new RequestJSON(request.id, request.exchange.exchangeCode.name(), request.assetType.name(), request.requestType.name(), request.quantity, request.pricePerUnit));
        }
        return requestJSONS;
    }
}
