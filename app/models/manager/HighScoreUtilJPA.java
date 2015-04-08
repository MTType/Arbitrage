package models.manager;

import java.util.Collections;
import java.util.List;
import models.entity.HighScore;
import models.exception.ArbitrageException;
import play.Logger;
import play.db.jpa.Transactional;

public class HighScoreUtilJPA implements HighScoreUtil {
    
    public static final int MAX_SCORES = 5;

    /**
     * writes current high score
     * @param highScore
     * @throws ArbitrageException 
     */
    @Transactional
    public void writeScore(HighScore highScore) throws ArbitrageException {
 
        List<HighScore> highScores = HighScore.findAll();
        
        if (highScores == null || highScores.isEmpty()) {
            return;
        }
        
        Collections.sort(highScores);
        if (MAX_SCORES <= highScores.size()) {

            if (highScores.get(highScores.size() - 1).score < highScore.score) {
                highScores.get(highScores.size() - 1).delete();
                Logger.info("there are more than 5 high scores, the new high score is higher, saved new high score for " + highScore.name);
                new HighScore(highScore.name, highScore.score, highScore.iconId).save();
            }
        } else {
            Logger.info("saved new high score for " + highScore.name);
            new HighScore(highScore.name, highScore.score, highScore.iconId).save();
        }
    }

    @Transactional
    public List<HighScore> getHighestScores() throws ArbitrageException {
        List<HighScore> highScores = HighScore.findAll();
        Collections.sort(highScores);
        return highScores;
    }

}
