package models.manager;

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
    public void writeScore(HighScoreJSON highScore) throws ArbitrageException {
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
            
            if (minScore < highScore.getScore()) {
                minHighScore.delete();
                new HighScore(highScore.getName(), highScore.getScore(), highScore.getIconId()).save();
            }
        } else {
            new HighScore(highScore.getName(), highScore.getScore(), highScore.getIconId()).save();
        }
    }

    public List<HighScoreJSON> getHighestScores() throws ArbitrageException {
        return HighScore.find("order by score").fetch();
    }
    
}
