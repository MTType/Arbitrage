package models;

public class BuyRequest implements Request{
    private final String assetType;
    private final int asset;
    private final float unitPrice;

    public BuyRequest(String assetType, float targetPrice, int asset){
        this.assetType = assetType;
        this.unitPrice = targetPrice;
        this.asset = asset;
    }
        
    @Override
    public float getUnitPrice(){
        return unitPrice;
    }

    @Override
    public String getAssetType(){
        return this.assetType;
    }
    
    @Override
    public int getAssetAmount() {
        return asset;
    }
    
}
