package models.response;

public class HighScoreJSON {
    
    private String name;
    private int score;
    private int iconId;
    
    public HighScoreJSON(String name, int score, int iconId){
        this.name=name;
        this.score=score;
        this.iconId = iconId;
    }    

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }       

    public int getIconId() {
        return iconId;
    }
}
