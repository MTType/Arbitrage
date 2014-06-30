package controllers;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Message;
import models.entity.Request;
import models.enums.ExchangeCode;
import models.manager.ExchangeManager;
import models.manager.PlayerManager;
import play.Logger;
import play.mvc.*;
import models.response.RequestJSON;


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
    
    public static void getRequests() {
        Logger.info("    In the controller!!!");
        List<RequestJSON> requestJSONS = exchangeManager.getRequestJSONs(ExchangeCode.LSE);
        Logger.info(new Gson().toJson(requestJSONS));
        renderJSON(requestJSONS);
    }

}