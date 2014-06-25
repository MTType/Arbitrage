package models;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Random;

public class RequestsArray {

    private final LinkedList<Request> Requests = new LinkedList();
    private final int numberOfElements;
    private int buyVolume = 0;
    private int sellVolume = 0;
    DecimalFormat tdp = new DecimalFormat("###.##");
    DecimalFormat zdp = new DecimalFormat("###");
    private final Random rng = new Random();
    private final double sDev;
    private float meanSellPrice;
    private float meanBuyPrice;
    private float highBuy;
    private float lowSell;
    
    public RequestsArray(int numberOfElements, float sDev) {
        this.meanBuyPrice = 1.0f;
        this.meanSellPrice = 1.0f;
        this.sDev = sDev;
        this.numberOfElements = numberOfElements;
        for(int i = 0; i<numberOfElements; i++){
            float targetPrice;
            int sORb = rng.nextInt(2);
            
            if(sORb == 0){
                targetPrice = (float)(((rng.nextGaussian())*sDev) + meanSellPrice);
                int ass = ((rng.nextInt(4)+1));
                
                int aType = rng.nextInt(3);                
                switch(aType){
                    case 0: Requests.add(new SellRequest("Pork Bellies", (targetPrice*1.28f), (ass*40000)));
                        break;
                    case 1: Requests.add(new SellRequest("Frozen Orange Juice Concentrate", (targetPrice*1.57f), (ass*15000)));
                        break;
                    case 2: Requests.add(new SellRequest("Soybeans", (targetPrice*14.05f), (ass*5000)));
                }
            }
            if(sORb == 1){
                targetPrice = (float)(((rng.nextGaussian())*sDev)+ meanBuyPrice);
                int ass = ((rng.nextInt(4)+1));
                
                int aType = rng.nextInt(3);                
                switch(aType){
                    case 0: Requests.add(new BuyRequest("Pork Bellies", (targetPrice*1.28f), (40000*ass)));
                        break;
                    case 1: Requests.add(new BuyRequest("Frozen Orange Juice Concentrate", (targetPrice*1.57f), (ass*15000)));
                        break;
                    case 2: Requests.add(new BuyRequest("Soybeans", (targetPrice*14.05f), (ass*5000)));
                }
            }        
        }
        lowestSellPrice();
        highestBuyPrice();
    }
    
    public Request getRequest(int loc){
        return Requests.get(loc);
    }
    
    public void removeRequest(int loc){
        Requests.remove(loc);
        float targetPrice;

        int sORb = rng.nextInt(2);
        if(sORb==0){
            
            targetPrice = (float)(((rng.nextGaussian())*sDev) + meanSellPrice + (sellVolume*sDev*0.03));
            if(targetPrice<0){
                targetPrice = 0;
            }
            int ass = ((rng.nextInt(4)+1));            
            int aType = rng.nextInt(3);                
                switch(aType){
                    case 0: Requests.add(new SellRequest("Pork Bellies", (targetPrice*1.28f), (ass*40000)));
                        break;
                    case 1: Requests.add(new SellRequest("Frozen Orange Juice Concentrate", (targetPrice*1.57f), (ass*15000)));
                        break;
                    case 2: Requests.add(new SellRequest("Soybeans", (targetPrice*14.05f), (ass*5000)));
                }
        }
        if(sORb==1){
            
            targetPrice = (float)(((rng.nextGaussian())*sDev) + meanBuyPrice - (buyVolume*sDev*0.03));
            if(targetPrice<0){
                targetPrice = 0;
            }
            int ass = ((rng.nextInt(4)+1));
            int aType = rng.nextInt(3);                
                switch(aType){
                    case 0: Requests.add(new BuyRequest("Pork Bellies", (targetPrice*1.28f), (40000*ass)));
                        break;
                    case 1: Requests.add(new BuyRequest("Frozen Orange Juice Concentrate", (targetPrice*1.57f), (ass*15000)));
                        break;
                    case 2: Requests.add(new BuyRequest("Soybeans", (targetPrice*14.05f), (ass*5000)));
                }
        }
        printRequest(Requests.size()-1);
        lowestSellPrice();
        highestBuyPrice();
    }
    
    public void addAndShift(){ //removes the oldest request and adds a new one
        Requests.remove(0);
        float targetPrice;

        int sORb = rng.nextInt(2);
        if(sORb==0){
            targetPrice = (float)(((rng.nextGaussian())*sDev) + meanSellPrice + (sellVolume*sDev*0.03));
            if(targetPrice<0){
                targetPrice = 0;
            }
            int ass = ((rng.nextInt(4)+1));            
            int aType = rng.nextInt(3);                
                switch(aType){
                    case 0: Requests.add(new SellRequest("Pork Bellies", (targetPrice*1.28f), (ass*40000)));
                        break;
                    case 1: Requests.add(new SellRequest("Frozen Orange Juice Concentrate", (targetPrice*1.57f), (ass*15000)));
                        break;
                    case 2: Requests.add(new SellRequest("Soybeans", (targetPrice*14.05f), (ass*5000)));
                }
        }
        if(sORb==1){
            targetPrice = (float)(((rng.nextGaussian())*sDev) + meanBuyPrice - (buyVolume*sDev*0.03));
            if(targetPrice<0){
                targetPrice = 0;
            }
            int ass = ((rng.nextInt(4)+1));
            int aType = rng.nextInt(3);                
                switch(aType){
                    case 0: Requests.add(new BuyRequest("Pork Bellies", (targetPrice*1.28f), (40000*ass)));
                        break;
                    case 1: Requests.add(new BuyRequest("Frozen Orange Juice Concentrate", (targetPrice*1.57f), (ass*15000)));
                        break;
                    case 2: Requests.add(new BuyRequest("Soybeans", (targetPrice*14.05f), (ass*5000)));
                }
        }
        if(sellVolume > 0){
            sellVolume = sellVolume -1;
        }
        if(buyVolume > 0){
            buyVolume = buyVolume -1;
        }
        printRequest(Requests.size()-1);
        lowestSellPrice();
        highestBuyPrice();
    }
    
    public float getHigh(){
        return highBuy;
    }
    
    public float getLow(){
        return lowSell;
    }
        
    public int getSellVolume() {
        return sellVolume;
    }
    
    public int getBuyVolume() {
        return buyVolume;
    }

    public void incBuyVolume() {
        this.buyVolume = buyVolume + 1;
    }
    
    public void decBuyVolume() {
        if(buyVolume -1 >=0){
            this.buyVolume = buyVolume - 1;
        }
    }
    
    public void incSellVolume() {
        this.sellVolume = sellVolume + 1;
    }
    
    public void decSellVolume() {
        if(sellVolume -1 >=0){
            this.sellVolume = sellVolume - 1;
        }
    }
    
    private void lowestSellPrice(){ 
        if (Requests.isEmpty()) {
              highBuy = 0;
        }       
        
        float lowest;
        lowest = meanSellPrice;
        
        for (Request RequestX : Requests) {
            if (RequestX instanceof SellRequest) {
                if (lowest < RequestX.getUnitPrice()) {
                    lowest = RequestX.getUnitPrice();
                }
            }
        }                  
        lowSell = lowest;
    }
    
    private void highestBuyPrice(){
        if (Requests.isEmpty()) {
              highBuy = 0;
        }       
        
        float highest;
        highest = meanBuyPrice;
        
        for (Request RequestX : Requests) {
            if (RequestX instanceof BuyRequest) {
                if (highest < RequestX.getUnitPrice()) {
                    highest = RequestX.getUnitPrice();
                }
            }
        }        
        highBuy = highest;
    }
    
    public void printRequests(){
        int i = 1;
        for (Request RequestX : Requests) {            
            if(RequestX instanceof BuyRequest){
                System.out.println(i + ": Buying " + RequestX.getAssetAmount() + " " + RequestX.getAssetType() + " at £" + tdp.format(RequestX.getUnitPrice()) + " totalling £" + zdp.format(RequestX.getAssetAmount() * RequestX.getUnitPrice()));
            }
            else if(RequestX instanceof SellRequest){
                System.out.println(i + ": Selling " + RequestX.getAssetAmount() + " " + RequestX.getAssetType() + " at £" + tdp.format(RequestX.getUnitPrice()) + " totalling £" + zdp.format(RequestX.getAssetAmount() * RequestX.getUnitPrice()));
            }
            i++;
        }
    }
    
    public void printRequest(int loc){
        if(Requests.get(loc) instanceof BuyRequest){
            System.out.println("Buying " + Requests.get(loc).getAssetAmount() + " " + Requests.get(loc).getAssetType() + " at £" + tdp.format(Requests.get(loc).getUnitPrice()) + " totalling £" + zdp.format(Requests.get(loc).getAssetAmount() * Requests.get(loc).getUnitPrice()));
        }
        else if(Requests.get(loc) instanceof SellRequest){
            System.out.println("Selling " + Requests.get(loc).getAssetAmount() + " " + Requests.get(loc).getAssetType() + " at £" + tdp.format(Requests.get(loc).getUnitPrice()) + " totalling £" + zdp.format(Requests.get(loc).getAssetAmount() * Requests.get(loc).getUnitPrice()));
        }
    }
}
