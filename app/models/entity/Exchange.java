

package models.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import models.enums.ExchangeCode;
import play.db.jpa.Model;

@Entity
public class Exchange extends Model {
    
    public ExchangeCode exchangeCode;
    public int buyVolume = 0;
    public int sellVolume = 0;
    public float meanSellPrice = 0;
    public float meanBuyPrice = 0;
    public float standardDeviation;
    @ElementCollection
    public List<Request> requests = new ArrayList<Request>();
    
    public Exchange(ExchangeCode exchangeCode, float meanSellPrice, float meanBuyPrice, float standardDeviation) {
        this.exchangeCode = exchangeCode;
        this.meanSellPrice = meanSellPrice;
        this.meanBuyPrice = meanBuyPrice;
        this.standardDeviation = standardDeviation;
    }
    
}
