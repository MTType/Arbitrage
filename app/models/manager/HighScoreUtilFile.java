package models.manager;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import models.exception.ArbitrageException;
import models.response.HighScoreJSON;
import play.Logger;

public class HighScoreUtilFile {

    private final static String HIGHSCORE_FILE = new File("").getAbsolutePath() + File.separator + "bigbang_highscores.txt";
    
    public void writeScore(HighScoreJSON highScore) throws ArbitrageException {
        File file = new File(HIGHSCORE_FILE);
        if (!file.exists()) {
            file.mkdirs(); 
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new ArbitrageException("Error creating new high score file: " + HIGHSCORE_FILE + " -> " + ex);
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE, true));
            writer.newLine(); 
            Logger.info("writing new high score: " + new Gson().toJson(highScore)); 
            writer.write(new Gson().toJson(highScore)); 
            writer.close(); 
        } catch (IOException ex) {
            throw new ArbitrageException("Error writing to high score file: " + HIGHSCORE_FILE + " -> " + ex); 
        }
    }
    
    public List<HighScoreJSON> getHighestScores() throws ArbitrageException {
        List<HighScoreJSON> highScores = new LinkedList<HighScoreJSON>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE));
            
            String nextLine;
            while ((nextLine = reader.readLine()) != null) { 
                try{
                    if (new Gson().fromJson(nextLine, HighScoreJSON.class) != null) { 
                        HighScoreJSON newHighScore = new Gson().fromJson(nextLine, HighScoreJSON.class);
                        
                        int i=0; 
                        boolean scoreInserted = false;
                        while (i<highScores.size() && !scoreInserted) {
                            if (highScores.get(i).getScore() < newHighScore.getScore()) {
                                highScores.add(i, newHighScore); 
                                scoreInserted = true;
                            }
                            i++;
                        }
                        if (!scoreInserted ) {
                            highScores.add(newHighScore);
                        }
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
     
    public List<HighScoreJSON> getAllScores() throws ArbitrageException {
        List<HighScoreJSON> highScores = new ArrayList<HighScoreJSON>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE));
            
            String nextLine;
            while ((nextLine = reader.readLine()) != null) { 
                try{
                    if (new Gson().fromJson(nextLine, HighScoreJSON.class) != null) { 
                        HighScoreJSON newHighScore = new Gson().fromJson(nextLine, HighScoreJSON.class);
                        
                        if (highScores.isEmpty()) {
                            highScores.add(newHighScore);
                        } else {
                            for(int i=0; i<highScores.size(); i++) {
                                if (highScores.get(i).getScore() < newHighScore.getScore()) {
                                    highScores.add(i, newHighScore); 
                                }
                            }
                        }
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
}
