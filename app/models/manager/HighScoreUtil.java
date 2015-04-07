package models.manager;

import java.util.List;
import models.exception.ArbitrageException;
import models.response.HighScoreJSON;


public interface HighScoreUtil {

    public void writeScore(HighScoreJSON highScore) throws ArbitrageException;

    public List<HighScoreJSON> getHighestScores() throws ArbitrageException;
    
}
