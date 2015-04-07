
package models.entity;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class HighScore extends Model {
    
    public String name;
    public int score;
    public int iconId;
    
    public HighScore(String name, int score, int iconId){
        this.name=name;
        this.score=score;
        this.iconId = iconId;
    }    
}
