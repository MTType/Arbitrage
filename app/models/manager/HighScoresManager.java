package models.manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.response.HighScoresJSON;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HighScoresManager {

    public static void writeScores(String name, int score){
        JSONObject hsJSON = new JSONObject();
        hsJSON.put("name", name);
        hsJSON.put("score", score);
        
        try {
 
		FileWriter highScores = new FileWriter("c:\\ArbitrageHighScores.json",true);
		highScores.write(hsJSON.toJSONString());
		highScores.flush();
		highScores.close();
 
	} catch (IOException e) {
		e.printStackTrace();
	}
           
    }    
    
    public static List<HighScoresJSON> readScores(){
        JSONParser parser = new JSONParser();
        List<HighScoresJSON> highScoresList = new ArrayList<HighScoresJSON>    
 

        Object obj = parser.parse(new FileReader("c:\\ArbitrageHighScores.json"));
 
	JSONObject highScores = (JSONObject) obj;
 
	String name = (String) highScores.get("name"); 
	int score = (Integer) highScores.get("score");
                
                    
        for (HighScoresJSON scores: highScoresList) {
            highScoresList.add(new HighScoresJSON(scores.name, scores.score));
        }
	        
        return highScoresList;
     }
        
}
    

