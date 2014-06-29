package controllers;

import java.util.List;
import models.entity.Request;
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
        new PlayerManager().createPlayer("Bobmus", 1000);
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
    
    public static void test() {
        Logger.info("    In the controller!!!");
        List<Request> requests = Request.findAll();
        Logger.info("    returning info about this request: ");
        exchangeManager.printRequest(requests.get(0).getId());
        renderText(requests.get(0).id);
    }

}