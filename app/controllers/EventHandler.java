package controllers;

import play.libs.F;

public class EventHandler {
    
    public static EventHandler instance = new EventHandler();
    public final F.EventStream event = new F.EventStream();
 
    private EventHandler() { }
}
