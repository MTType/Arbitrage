package controllers;

import com.google.gson.Gson;
import java.util.List;
import java.util.logging.Level;
import models.entity.Player;
import models.entity.Request;
import models.enums.ExchangeCode;
import models.exception.ArbitrageException;
import models.manager.ExchangeManager;
import models.manager.HighScoreUtil;
import models.manager.PlayerManager;
import models.response.HighScoreJSON;
import models.response.RequestJSON;
import play.Logger;
import play.mvc.*;
import play.test.Fixtures;


public class Application extends Controller {
    
    private static final PlayerManager playerManager = new PlayerManager();
    private static final ExchangeManager exchangeManager = new ExchangeManager();
    private static final int DEFAULT_REQUEST_SIZE = 10;
    private static final float DEFAULT_SD = 0.2f;
    
    public static void index() {
        Logger.info("Resetting DB");
        Fixtures.deleteDatabase();
        render();
    }
    
    public static void player() {
        new PlayerManager().createPlayer("Owen", 5000);
        render();
    }
    
    public static void game() {
        for (ExchangeCode exchangeCode: ExchangeCode.values()) {
            exchangeManager.createExchange(exchangeCode, DEFAULT_REQUEST_SIZE, DEFAULT_SD);
        } 
        Logger.info("Game initialised");
        exchangeManager.printRequests();
        render();
    }
    
    public static void exit() {
        Player player = playerManager.getPlayer();
        try {
            HighScoreUtil.writeScore(new HighScoreJSON(player.name, player.cash));
        } catch (ArbitrageException ex) {
            Logger.info("Arbitrage exception when saving high score " + ex.getMessage());
        }
        renderTemplate("Application/index.html");
    }
    
    public static void getRequests(String exchangeCode) {
        List<RequestJSON> requestJSONS = exchangeManager.getRequestJSONs(ExchangeCode.valueOf(exchangeCode.toUpperCase()));
        renderJSON(requestJSONS);
    }
    
    public static void getPlayer(){
        Logger.info("Getting player info "); 
        Logger.info(new Gson().toJson(playerManager.getPlayerJSON())); 
        renderJSON(playerManager.getPlayerJSON());
    }
    
    public static void acceptRequest(String exchangeCode, long requestId){
        Request request = Request.findById(requestId);
        if (request == null) {
            Logger.info("requets not found");
            renderText("ERROR - request with id " + requestId + " not found!");
        }
        try {
            playerManager.acceptOffer(request);
            exchangeManager.removeRequest(ExchangeCode.valueOf(exchangeCode.toUpperCase()), requestId);
            Logger.info("successfully accepted request");
            renderText("OK");
        } catch (ArbitrageException ex) {
            Logger.info("Arbitrage exception!! " + ex.getMessage());
            renderText(ex.getMessage());
        }
    }
        
    public static void getHighScores () {
        List<HighScoreJSON> scores;
        try {
            scores = HighScoreUtil.getHighestScores();
            renderJSON(scores);
        } catch (ArbitrageException ex) {
            Logger.error("Arbitrage exception, can't read scores: " + ex.getMessage());
        }
    }
    
    public static void setHighScore(String name, int cash) {
        HighScoreJSON newHighScore = new HighScoreJSON(name, cash);
        try {
            HighScoreUtil.writeScore(newHighScore);
        } catch (ArbitrageException ex) {
            Logger.error("Arbitrage exception, can't write score to file: " + ex.getMessage());
        }
    }

    public static class RequestSocket extends WebSocketController {
    
        public static void requestUpdate() {
            Logger.info("In the websocket");
            while(inbound.isOpen()) {
                Logger.info("websocket is open");
                String message = (String)await(EventHandler.instance.event.nextEvent());
                Logger.info("websocket has a new event");  
                
                outbound.sendJson(message);
            }
        }
    }
    
}
