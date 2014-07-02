
package models.enums;


public enum ExchangeCode {
    
    NYSE ("NYSE"),
    NASDAQ ("NASDAQ"),
    LSE ("LSE");
    
    private final String name;

    private ExchangeCode(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
