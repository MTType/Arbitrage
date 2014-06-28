
package models.enums;


public enum ExchangeCode {
    
    NYSE ("NYSE"),
    NASDAQ ("NASDAQ"),
    LSE ("LSE");
    
    private String name;

    private ExchangeCode(String name) {
        this.name = name;
    }
}
