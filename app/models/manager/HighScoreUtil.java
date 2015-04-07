package models.manager;

import java.util.List;
import models.entity.HighScore;
import models.exception.ArbitrageException;


public interface HighScoreUtil {

    public void writeScore(HighScore highScore) throws ArbitrageException;

    public List<HighScore> getHighestScores() throws ArbitrageException;
        
}
