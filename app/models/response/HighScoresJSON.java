package models.response;

public class HighScoresJSON {
    
    private String name;
    private int score;
    
    public HighScoresJSON(String name, int score){
        this.name=name;
        this.score=score;
    }    

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }       
}
