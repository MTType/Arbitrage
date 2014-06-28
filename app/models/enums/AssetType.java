
package models.enums;

public enum AssetType {
    
    OJ ("Frozen Orange Juice"),
    PB ("Pork Bellies"),
    SB ("Soy Beans");
    
    private String name;

    private AssetType(String name) {
        this.name = name;
    }

}
