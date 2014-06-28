package models.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import models.enums.AssetType;
import models.enums.RequestType;
import play.db.jpa.Model;

@Entity
public class Request extends Model {
 
    @OneToOne
    public Exchange exchange;
    public AssetType assetType;
    public RequestType requestType;
    public int quantity;
    public int pricePerUnit;
    public Date timestamp;
    
    public Request(Exchange exchange, AssetType assetType, RequestType requestType, int quantity, int pricePerUnit, Date timestamp) {
        this.exchange = exchange;
        this.assetType = assetType;
        this.requestType = requestType;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.timestamp = timestamp;
    }
    
}
