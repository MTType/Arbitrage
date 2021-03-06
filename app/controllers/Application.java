package controllers;

import java.util.List;
import models.entity.HighScore;
import models.entity.Player;
import models.entity.Request;
import models.enums.ExchangeCode;
import models.exception.ArbitrageException;
import models.manager.ExchangeManager;
import models.manager.HighScoreUtil;
import models.manager.HighScoreUtilJPA;
import models.manager.PlayerManager;
import models.response.RequestJSON;
import play.Logger;
import play.mvc.*;
import play.mvc.results.*;



public class Application extends Controller {
    
    private static final PlayerManager playerManager = new PlayerManager();
    private static final ExchangeManager exchangeManager = new ExchangeManager();
    private static final HighScoreUtil highScoreUtil = new HighScoreUtilJPA();
    private static final int DEFAULT_REQUEST_SIZE = 10;
    private static final float DEFAULT_SD = 0.2f;
    
    public static void index() {
        resetDB();
        render();
    }
    
    public static void player() {
        resetDB();
        render();
    }
    
    public static void game() {
        int offSet = 0;
        for (ExchangeCode exchangeCode: ExchangeCode.values()) {
            exchangeManager.createExchange(exchangeCode, DEFAULT_REQUEST_SIZE, DEFAULT_SD, offSet);
            offSet = offSet + 2;
        } 
        Logger.info("Game initialised");
        exchangeManager.printRequests();
        render();
    }
    
    public static void exit() {
        Player player = playerManager.getPlayer();
        try {
            highScoreUtil.writeScore(new HighScore(player.name, player.cash, player.iconId));
        } catch (ArbitrageException ex) {
            Logger.info("Arbitrage exception when saving high score " + ex.getMessage());
        }        
        resetDB();
        renderTemplate("Application/index.html");
    }
    
    public static void endscreen(){
        HighScore newHighScore = new HighScore(playerManager.getPlayer().name, playerManager.getPlayer().cash, playerManager.getPlayer().iconId);
        Logger.info("attempting to pass new high score for player " + playerManager.getPlayer().name);
        try {
            highScoreUtil.writeScore(newHighScore);
        } catch (ArbitrageException ex) {
            Logger.error("Arbitrage exception, can't write score to file: " + ex.getMessage());
        }
        render();
    }
    
    public static void getRequests(String exchangeCode) {
        List<RequestJSON> requestJSONS = exchangeManager.getRequestJSONs(ExchangeCode.valueOf(exchangeCode.toUpperCase()));
        renderJSON(requestJSONS);
    }
    
    public static void getPlayer(){
        renderJSON(playerManager.getPlayerJSON());
    }
    
    public static void acceptRequest(String exchangeCode, long requestId){
        Request request = Request.findById(requestId);
        if (request == null) {
            Logger.info("request not found");
            renderText("ERROR - request with id " + requestId + " not found!");
        }
        try {
            playerManager.acceptOffer(request);
            exchangeManager.removeRequest(ExchangeCode.valueOf(exchangeCode.toUpperCase()), requestId);
            EventHandler.instance.event.publish("refresh");
            Logger.info("successfully accepted request");
            renderText("OK");
        } catch (ArbitrageException ex) {
            Logger.info("Arbitrage exception!! " + ex.getMessage());
            renderText(ex.getMessage());
        }
    }
        
    public static void getHighScores () {
        List<HighScore> scores;
        try {
            scores = highScoreUtil.getHighestScores();
            renderJSON(scores);
        } catch (ArbitrageException ex) {
            Logger.error("Arbitrage exception, can't read scores: " + ex.getMessage());
        }
    }
    

    public static void setHighScore(String name, int cash, int iconId) {
        HighScore newHighScore = new HighScore(name, cash, iconId);
        try {
            highScoreUtil.writeScore(newHighScore);
        } catch (ArbitrageException ex) {
            Logger.error("Arbitrage exception, can't write score to file: " + ex.getMessage());
        }
    }

    public static class RequestSocket extends WebSocketController {
    
        public static void requestUpdate() {
            while(inbound.isOpen()) {
                String message = (String)await(EventHandler.instance.event.nextEvent());
                outbound.sendJson(message);
            }
            Logger.error("inbound not open");
        }
    }
    
    public static void newPlayer(String name, int startingCash, int iconId) { 
        if (playerManager.getPlayer() == null) {
            resetDB();
            playerManager.createPlayer(name, startingCash, iconId);
            renderText("OK");
        } else {
            Logger.info("player already exists, there must still be a game going on, returning error");
            renderText("IN_PROGRESS");
        }
    }
    
    public static void getPlayerInformation() {
        renderJSON(playerManager.getPlayerJSON());
    }
    

    private static void resetDB() {
        Logger.info("Resetting game-instance data");
        exchangeManager.deleteAllRequestsAndExchanges();
        Logger.info("removed all requests and exchange");
        playerManager.deleteAllPlayers();
    }
}
