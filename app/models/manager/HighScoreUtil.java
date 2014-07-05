

package models.manager;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.exception.ArbitrageException;
import models.response.HighScoreJSON;
import play.Logger;

public class HighScoreUtil {
    //final -> we never want this variable to be changed, Java enforces this with final
    //careful with final Objects, you're making the reference to the object final, the object itself can change
    private final static String HIGHSCORE_FILE = "C:\\Users\\Martyn\\Desktop\\highscores";
    
    public static void writeScore(HighScoreJSON highScore) throws ArbitrageException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE, true)); //FileWriter's "true" cosntructor argument makes it append instead of overwriting.
            writer.newLine(); //We want to parse an object per line to make things simple and more readable
            Logger.info("writing new high score: " + new Gson().toJson(highScore)); //In development let's write something contextual to the method, remove when releasing the code to production
            writer.write(new Gson().toJson(highScore)); //Google has a library that will take an object and write it in the form {name1:"value1", name2:"value2"}. This is just a convenience for us to know how to read it (aka deserialise)
            writer.close(); //never forget to close the data stream!
        } catch (IOException ex) {
            throw new ArbitrageException("Error writing to high score file: " + HIGHSCORE_FILE + " -> " + ex); //We'll consolidate all exceptions to one exception that we can handle ourselves and at the same time make it relevant to the situation we're in.
        }
    }
    
    public static List<HighScoreJSON> getScores() throws ArbitrageException {
        List<HighScoreJSON> highScores = new ArrayList<HighScoreJSON>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE));
            
            String nextLine;
            while ((nextLine = reader.readLine()) != null) { //while there are more lines to be read
                try{
                    if (new Gson().fromJson(nextLine, HighScoreJSON.class) != null) { //this method can return null if it can't deserialise properly, we need to guard ourselves against this.
                        highScores.add(new Gson().fromJson(nextLine, HighScoreJSON.class)); //Google library again - this deserialises strings and attempts to make an object out of them.
                    }
                    Logger.info("reading score: " + new Gson().fromJson(nextLine, HighScoreJSON.class));        
                } catch (Exception e) {
                    Logger.info("Skipping...  high score file line can't be parsed into a HighScoreJSON: " + nextLine); 
                }
            }
            reader.close(); //never forget to close the data stream!
        } catch (FileNotFoundException ex) {
            throw new ArbitrageException("high score file not found: " + HIGHSCORE_FILE + " -> " + ex);
        } catch (IOException ex) {
            throw new ArbitrageException("Error reading high score file: " + HIGHSCORE_FILE + " -> " + ex);
        }
        return highScores;
    }
    
    //we can't use a main method to test this class. 
    //In the background, play is building the project in its own way such that it won't recognise this as a main method.
    public static void main(String[] args) {

    }
    
    //Side note: public static methods are VERY bad practice. I can reimplement with a better solution, but we would need to discuss a Java pattern called Singleton.
}
