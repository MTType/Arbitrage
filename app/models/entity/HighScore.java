
package models.entity;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class HighScore extends Model implements Comparable<HighScore> {
    
    public String name;
    public int score;
    public int iconId;
    
    public HighScore(String name, int score, int iconId){
        this.name=name;
        this.score=score;
        this.iconId = iconId;
    }    

    public int compareTo(HighScore o) {
        if (o == null) {
            return this.score;
        } 
        return o.score - this.score;
    }
}
