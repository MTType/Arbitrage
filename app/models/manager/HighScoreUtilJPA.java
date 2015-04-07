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
        List<HighScore> highScores = HighScore.find("order by score").fetch();
        
        if (highScores == null || highScores.size() == 0) {
            return;
        }
        
        if (MAX_SCORES >= 5) {
            if (highScores.get(highScores.size()-1).score < highScore.getScore()) {
                highScores.get(highScores.size()-1).delete();
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
