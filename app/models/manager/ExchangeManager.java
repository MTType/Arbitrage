
package models.manager;

import controllers.EventHandler;
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
    private static final int REQUEST_EXPIRE_TIME = 50;
    private static final int DEFAULT_QUANTITY_PB = 100;
    private static final int DEFAULT_QUANTITY_OJ = 200;
    private static final int DEFAULT_QUANTITY_SB = 400;
    
    
    @Transactional
    public void createExchange(ExchangeCode exchangeCode, int numberOfRequests, float standardDeviation, int offSet) {
        Exchange exchange = new Exchange(exchangeCode, 1.0f, 1.0f, standardDeviation).save(); 
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, offSet);
        for(int i = 0; i<numberOfRequests; i++){
            calendar.add(Calendar.SECOND, 6);
            addRequest(exchange, calendar.getTime());    
        }
        exchange.save();
        
        EventHandler.instance.event.publish("refresh");
    }
    
    private Exchange getExchange(ExchangeCode exchangeCode) {
        return Exchange.find("byExchangeCode", exchangeCode).first();
    }
    
    @Transactional
    private void addRequest(Exchange exchange) {
        addRequest(exchange, getRequestExpireTime());
    }
    
    @Transactional
    private void addRequest(Exchange exchange, Date expiryTime) {
        float targetPrice;
        int sORb = rng.nextInt(2);

        int quantity = ((rng.nextInt(4)+1));
        int assetTypeNumber = rng.nextInt(3); 
        Request request;
        if (sORb == 0) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation) + exchange.meanSellPrice);
            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.SELL, (quantity*DEFAULT_QUANTITY_PB), (int)(targetPrice*6.0f), expiryTime).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.SELL, (quantity*DEFAULT_QUANTITY_OJ), (int)(targetPrice*12.0f), expiryTime).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.SELL, (quantity*DEFAULT_QUANTITY_SB), (int)(targetPrice*24.0f), expiryTime).save();
                    request.save();
                    printRequest(request.id);
            }
        }
        if (sORb == 1) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation)+ exchange.meanBuyPrice);

            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.BUY, (quantity*DEFAULT_QUANTITY_PB), (int)(targetPrice*6.0f), expiryTime).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.BUY, (quantity*DEFAULT_QUANTITY_OJ), (int)(targetPrice*12.0f), expiryTime).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.BUY, (quantity*DEFAULT_QUANTITY_SB), (int)(targetPrice*24.0f), expiryTime).save();
                    request.save();
                    printRequest(request.id);
            }
        }
        exchange.save();
    }
    
    @Transactional
    public void removeOldRequests(Exchange exchange) {
        List<Request> exchangeRequests = Request.find("byExchange", exchange).fetch();
        for (Request request: exchangeRequests) {
            if (request.expiretime.before(new Date())) {
                //Logger.info("removing expired request with timestamp: " + request.expiretime);
                removeRequest(exchange, request.id);
            }
        }

        EventHandler.instance.event.publish("refresh");
    }
    
    @Transactional
    public void removeRequest(ExchangeCode exchangeCode, long id){
        removeRequest(getExchange(exchangeCode), id);
    }
    
    private Date getRequestExpireTime() {
        Calendar newExpireTime = Calendar.getInstance();
        newExpireTime.add(Calendar.SECOND, REQUEST_EXPIRE_TIME);
        return newExpireTime.getTime(); 
    }
    
    @Transactional
    public void removeRequest(Exchange exchange, long id){
        Request requestToRemove = null;
        for (Request request: RequestManager.getExchangeRequests(exchange)) { 
            if (request.id == id) {
                requestToRemove = request;
            }
        }
        if (requestToRemove == null) {
            return;
        }
        
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
                    request = new Request(exchange, AssetType.PB, RequestType.SELL, (quantity*DEFAULT_QUANTITY_PB), (int)(targetPrice*6.0f), getRequestExpireTime()).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.SELL, (quantity*DEFAULT_QUANTITY_OJ), (int)(targetPrice*12.0f), getRequestExpireTime()).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.SELL, (quantity*DEFAULT_QUANTITY_SB), (int)(targetPrice*24.0f), getRequestExpireTime()).save();
                    request.save();
                    printRequest(request.id);
            }
        } else if(sORb==1) {
            targetPrice = (float)(((rng.nextGaussian())*exchange.standardDeviation) + exchange.meanBuyPrice - (exchange.buyVolume*exchange.standardDeviation*0.03));
            if (targetPrice<0) {
                targetPrice = 0;
            }
            switch(assetTypeNumber){
                case 0: 
                    request = new Request(exchange, AssetType.PB, RequestType.BUY, (quantity*DEFAULT_QUANTITY_PB), (int)(targetPrice*6.0f), getRequestExpireTime()).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 1: 
                    request = new Request(exchange, AssetType.OJ, RequestType.BUY, (quantity*DEFAULT_QUANTITY_OJ), (int)(targetPrice*12.0f), getRequestExpireTime()).save();
                    request.save();
                    printRequest(request.id);
                    break;
                case 2: 
                    request = new Request(exchange, AssetType.SB, RequestType.BUY, (quantity*DEFAULT_QUANTITY_SB), (int)(targetPrice*24.0f), getRequestExpireTime()).save();
                    request.save();
                    printRequest(request.id);
            }
        }
        
        exchange.save();  
    }
    
    @Transactional
    public void printRequests(){
        for (ExchangeCode exchangeCode: ExchangeCode.values()) {
            List<Request> exchangeRequests = Request.find("byExchange", Exchange.find("byExchangeCode", exchangeCode).fetch()).fetch();
            for (Request request: exchangeRequests) {
                //Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at " + request.pricePerUnit + " per unit, totalling " + (request.quantity * request.pricePerUnit) + "    Expires at this time: " + request.expiretime);
            }
        }
    }
    
    @Transactional
    public void printRequests(Exchange exchange){
        List<Request> exchangeRequests = Request.find("byExchange", exchange).fetch();
        for (Request request: exchangeRequests) {
            //Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at " + request.pricePerUnit + " per unit, totalling " + (request.quantity * request.pricePerUnit) + "    Expires at this time: " + request.expiretime);
        }
    }
    
    @Transactional
    public void printRequest(Exchange exchange, int loc){
        List<Request> exchangeRequests = Request.find("byExchange", exchange).fetch();
        if (loc < exchangeRequests.size() && loc > 0) {
            Request request = exchangeRequests.get(loc);
            //Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at " + request.pricePerUnit + " per unit, totalling " + (request.quantity * request.pricePerUnit) + "    Expires at this time: " + request.expiretime);
        }
    }
    
    @Transactional
    public void printRequest(long requestId){
        Request request = Request.findById(requestId);
        //Logger.info(request.requestType.name() + " request. " + request.quantity + " " + request.assetType + " at " + request.pricePerUnit + " per unit, totalling " + (request.quantity * request.pricePerUnit) + "    Expires at this time: " + request.expiretime);
    }
    
    @Transactional
    public void addAndShift(Exchange exchange){
        List<Request> requests = RequestManager.getExchangeRequests(exchange);
        Request oldestRequest = requests.get(0);
        for (Request request: requests) {
            oldestRequest = oldestRequest.expiretime.before(request.expiretime) ? oldestRequest : request;
        }
        
        oldestRequest.delete();
        exchange.save();
        
        addRequest(exchange);
        
        if(exchange.sellVolume > 0){
            exchange.sellVolume--;
        }
        if(exchange.buyVolume > 0){
            exchange.buyVolume--;
        }
        exchange.save();
        
        printRequest(exchange, requests.size()-1);
    }
    
    public List<RequestJSON> getRequestJSONs(ExchangeCode exchangeCode) {
        List<Request> requests = Request.find("select r from Request r where r.exchange = ? order by r.expiretime desc", getExchange(exchangeCode)).fetch();
        List<RequestJSON> requestJSONS = new ArrayList<RequestJSON>();
        for (Request request: requests) {
            requestJSONS.add(new RequestJSON(request.id, request.exchange.exchangeCode.name(), request.assetType.name(), request.requestType.name(), request.quantity, request.pricePerUnit));
        }
        return requestJSONS;
    }
}
