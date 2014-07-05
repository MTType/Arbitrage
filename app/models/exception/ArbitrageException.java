
package models.exception;

public class ArbitrageException extends Exception {
    
    private String message;
    
    public ArbitrageException (String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
