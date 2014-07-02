package models.response;

public class RequestJSON {
   
    private float id;
    private String exchangeCode;
    private String assetTypeString;
    private String requestTypeString;
    private int quantity;
    private int pricePerUnit;

    public RequestJSON(float id, String exchangeCode, String assetTypeString, String requestTypeString, int quantity, int pricePerUnit) {
        this.id = id;
        this.exchangeCode = exchangeCode;
        this.assetTypeString = assetTypeString;
        this.requestTypeString = requestTypeString;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
    
    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }
    
    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getAssetTypeString() {
        return assetTypeString;
    }

    public void setAssetTypeString(String assetTypeString) {
        this.assetTypeString = assetTypeString;
    }

    public String getRequestTypeString() {
        return requestTypeString;
    }

    public void setRequestTypeString(String requestTypeString) {
        this.requestTypeString = requestTypeString;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

}
