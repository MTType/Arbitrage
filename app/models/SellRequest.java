package models;

public class SellRequest implements Request{
    private final String assetType;
    private final int asset;
    private final float unitPrice;

    public SellRequest(String assetType, float targetPrice, int asset){
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
        return assetType;
    }
    
    @Override
    public int getAsset() {
        return asset;
    }
    
}
