package controllers;

import com.google.gson.Gson;
import java.util.List;
import java.util.logging.Level;
import models.entity.Request;
import models.enums.ExchangeCode;
import models.exception.ArbitrageException;
import models.manager.ExchangeManager;
import models.manager.HighScoreUtil;
import models.manager.PlayerManager;
import models.response.HighScoreJSON;
import models.response.RequestJSON;
import play.Logger;
import play.db.jpa.GenericModel;
import play.mvc.*;
import play.test.Fixtures;


public class Application extends Controller {
    
    private static final PlayerManager playerManager = new PlayerManager();
    private static final ExchangeManager exchangeManager = new ExchangeManager();
    private static final int DEFAULT_REQUEST_SIZE = 10;
    private static final float DEFAULT_SD = 0.1f;
    
    public static void index() {
        Logger.info("Resetting DB");
        Fixtures.deleteDatabase();
        HighScoreJSON newHighScore = new HighScoreJSON("bobmus", 1000);
        HighScoreJSON newHighScore2 = new HighScoreJSON("steve", 2000);
        HighScoreJSON newHighScore3 = new HighScoreJSON("shamus", 3000);
        HighScoreJSON newHighScore4 = new HighScoreJSON("catman", 4000);
        try {
            HighScoreUtil.writeScore(newHighScore);
            HighScoreUtil.writeScore(newHighScore2);
            HighScoreUtil.writeScore(newHighScore3);
            HighScoreUtil.writeScore(newHighScore4);
            List<HighScoreJSON> scores = HighScoreUtil.getScores();
            Logger.info(""+ scores.size());
            for (HighScoreJSON score: scores) {
                Logger.info(score.getName() + " has a score of  " + score.getScore());
            }
        } catch (ArbitrageException ex) {
            Logger.info(ex.getMessage());
        }
        render();
    }
    
    public static void player() {
        new PlayerManager().createPlayer("Owen", 1000000);
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
    
    public static void play() {
        render();
    }
    
    public static void getRequests(String exchangeCode) {
        //Logger.info("Retrieving requests for exchange: " + exchangeCode);
        List<RequestJSON> requestJSONS = exchangeManager.getRequestJSONs(ExchangeCode.valueOf(exchangeCode.toUpperCase()));
        //Logger.info(new Gson().toJson(requestJSONS));
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
