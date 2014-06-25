package controllers;

import models.test.TestH2;
import play.mvc.*;


public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void player() {
        TestH2.createPlayer();
        render();
    }
    
    public static void game() {
        render();
    }
    
    public static void play() {
        render();
    }
   
}