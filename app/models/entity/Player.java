package models.entity;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Player extends Model {
    
    public String name;
    public float cash;
    public int pb;
    public int oj;
    public int sb;

}
