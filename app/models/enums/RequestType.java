
package models.enums;


public enum RequestType {
    
    BUY ("Buy"),
    SELL ("Sell");
    
    private String name;

    private RequestType(String name) {
                this.name = name;
    }

}
