package models.manager;

import com.google.gson.Gson;
import java.util.Collections;
import java.util.List;
import models.entity.HighScore;
import models.exception.ArbitrageException;
import models.response.HighScoreJSON;

public class HighScoreUtilJPA implements HighScoreUtil {
    
    public static final int MAX_SCORES = 5;

    /**
     * writes current high score
     * @param highScore
     * @throws ArbitrageException 
     */
    public void writeScore(HighScore highScore) throws ArbitrageException {
        List<HighScore> highScores = HighScore.findAll();
                
        if (highScores == null || highScores.size() == 0) {
            return;
        }
        
        if (MAX_SCORES >= 5) {
            int minScore = highScores.get(0).score;
            HighScore minHighScore = highScores.get(0);
            for (HighScore savedHighScore: highScores) {
                if (minScore < savedHighScore.score) {
                    minScore = savedHighScore.score;
                    minHighScore = highScores.get(0);
                }
            }
            
            if (minScore < highScore.score) {
                minHighScore.delete();
                new HighScore(highScore.name, highScore.score, highScore.iconId).save();
            }
        } else {
            new HighScore(highScore.name, highScore.score, highScore.iconId).save();
        }
    }

    public List<HighScore> getHighestScores() throws ArbitrageException {
        List<HighScore> highScores = HighScore.findAll();
        Collections.sort(highScores);
        return highScores;
    }

}
