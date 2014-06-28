package controllers;

import models.enums.ExchangeCode;
import models.manager.ExchangeManager;
import models.manager.PlayerManager;
import play.Logger;
import play.mvc.*;


public class Application extends Controller {
    
    private static ExchangeManager exchangeManager = new ExchangeManager();
    private static int DEFAULT_REQUEST_SIZE = 10;
    private static float DEFAULT_SD = 0.1f;
    
    public static void index() {
        Logger.info("HEYHEYHEY");
        render();
    }
    
    public static void player() {
        render();
    }
    
    public static void game() {
        new PlayerManager("Bobmus", 1000);
        for (ExchangeCode exchangeCode: ExchangeCode.values()) {
            exchangeManager.createExchange(exchangeCode, DEFAULT_REQUEST_SIZE, DEFAULT_SD);
        } 
        Logger.info("Game initialised");
        exchangeManager.printRequests();
        render();
    }
    
    public static void play() {
        render();
    }
    
    public static void doSomething() {
        
    }
}