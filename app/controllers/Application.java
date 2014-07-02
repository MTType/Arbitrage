package controllers;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Message;
import models.entity.Request;
import models.enums.ExchangeCode;
import models.manager.ExchangeManager;
import models.manager.HighScoresManager;
import models.manager.PlayerManager;
import play.Logger;
import play.mvc.*;
import models.response.RequestJSON;
import models.response.HighScoresJSON;


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
        new PlayerManager().createPlayer("Owen", 1000000);
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
    
    public static void getHighScores() {
        Logger.info("    Here cometh the highscores!!!");
        List<HighScoresJSON> HighScoresJSONS = /*HighScoresManager.ReadScores(ExchangeCode.LSE);*/
        Logger.info(new Gson().toJson(HighScoresJSONS));
        renderJSON(HighScoresJSONS);
    }

    public static void addScore(String name, int score){
        Logger.info(" Score added");
        HighScoresManager.writeScores(name, score);    
    }

}