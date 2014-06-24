package models;

public class Player{
    private final String name;
    private float cash;
    private int pb;
    private int oj;
    private int sb;
    
    public Player(String n, float startingCash){
        name = n;
        this.pb = 800;
        this.oj = 800;
        this.sb = 800;
        this.cash=startingCash;
    }
    
    public float getCash(){
        return cash;
    }
    
    public void setCash(float cashIn){
        cash = cashIn;
    }
    
    public void incCash(float increment){
        cash = cash + increment;
    }
   
    public int getAsset(String as){
        if ("Pork Bellies".equals(as)){
            return pb;
        }
        else if ("Frozen Orange Juice Concentrate".equals(as)){
            return oj;
        }
        else if ("Soybeans".equals(as)){
            return sb;
        }else throw new Error("No such asset");
        
    }
    
    public void setAsset(String as, int a){
        if ("Pork Bellies".equals(as)){
            pb = a;
        }
        else if ("Frozen Orange Juice Concentrate".equals(as)){
            oj = a;
        }
        else if ("Soybeans".equals(as)){
            sb = a;
        }else throw new Error("No such asset");
    }

    private void incAsset(String as, int inc){
        if ("Pork Bellies".equals(as)){
            pb = pb+inc;
        }
        else if ("Frozen Orange Juice Concentrate".equals(as)){
            oj = oj+inc;
        }
        else if ("Soybeans".equals(as)){
            sb = sb+inc;
        }else throw new Error("No such asset");
    }

    public boolean acceptOffer(Request RequestX){
        float cost = RequestX.getUnitPrice()*RequestX.getAsset();
        String assetType = RequestX.getAssetType();
        
        if(RequestX instanceof BuyRequest){
            if((this.getAsset(RequestX.getAssetType())-RequestX.getAsset())<0){
                //popup box tells user they do not have enough of that asset to perform that transaction
                System.out.println("You do not have enough of that asset to perform that transaction");
                return false;
            }else if((this.getAsset(RequestX.getAssetType())-RequestX.getAsset())>=0){
                this.incAsset(RequestX.getAssetType(), -(RequestX.getAsset()));
                cash = cash + cost;
                return true;
            }
        }
        else if(RequestX instanceof SellRequest){           
            if((cash - cost) < 0){
                //popup box tells user they do not have enough cash to perform that transaction
                System.out.println("You do not have enough cash to perform that transaction");
                return false;
            }
            else if((cash - cost) >=0){
                cash = cash - cost;
                this.incAsset(assetType, RequestX.getAsset());
                return true;
            }else throw new Error("Something went wrong with the player buying something");
        }else throw new Error("Somehow the request is neither a buy request or a sell request");
        return false;
    }
}
