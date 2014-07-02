package controllers;

import com.google.gson.Gson;
import java.util.List;
import models.enums.ExchangeCode;
import models.manager.ExchangeManager;
import models.manager.PlayerManager;
import models.response.RequestJSON;
import play.Logger;
import play.mvc.*;
import play.test.Fixtures;


public class Application extends Controller {
    
    private static final ExchangeManager exchangeManager = new ExchangeManager();
    private static final int DEFAULT_REQUEST_SIZE = 10;
    private static final float DEFAULT_SD = 0.1f;
    
    public static void index() {
        Logger.info("Resetting DB");
        Fixtures.deleteDatabase();
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
    
    public static void getRequests(String exchangeCode) {
        Logger.info("Retrieving requests for exchange: " + exchangeCode);
        Logger.info(ExchangeCode.LSE.getName());
        List<RequestJSON> requestJSONS = exchangeManager.getRequestJSONs(ExchangeCode.valueOf(exchangeCode.toUpperCase()));
        Logger.info(new Gson().toJson(requestJSONS));
        renderJSON(requestJSONS);
    }

    public static class RequestSocket extends WebSocketController {
    
        public static void requestUpdate() {
            Logger.info("In the websocket");
            while(inbound.isOpen()) {
                Logger.info("websocket is open");
                List<RequestJSON> e = (List<RequestJSON>)await(EventHandler.instance.event.nextEvent());
                Logger.info("websocket has a new event");  
                
                //for(String quit: TextFrame.and(Equals("quit")).match(e)) {
                //    outbound.send("Bye!");
                //    disconnect();
                //}

                outbound.sendJson(new Gson().toJson(e));
                
                //if (e instanceof String) {
                //   Logger.info("sending string " + e);
                //    outbound.send("Echo: %s", (String)e);
                //}
                
                //for(String message: TextFrame.match(e)) {
                //    Logger.info("sending message: " + message);
                //    outbound.send("Echo: %s", message);
                //}

                //for(Http.WebSocketClose closed: SocketClosed.match(e)) {
                //    Logger.info("Socket closed!");
                //}
            }
        }
    }
    
}