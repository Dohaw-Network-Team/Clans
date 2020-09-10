package net.dohaw.play.divisions.customitems;

import lombok.Getter;
import org.bukkit.Material;

public enum ItemType {

    WEAPON(Material.DIAMOND_SWORD),
    ARMOR(Material.IRON_CHESTPLATE),
    CONSUMABLE(Material.PORKCHOP),
    GEMSTONE(Material.DIAMOND);

    @Getter private Material menuMat;
    ItemType(Material menuMat){
        this.menuMat = menuMat;
    }

    /*
    public static ItemType getByMaterial(Material mat){
        for(ItemType it : ItemType.values()){
            if(it.menuMat == mat){
                return it;
            }
        }
        return null;
    }*/

    public static ItemType getNextItemType(ItemType itemType){
        ItemType[] types = ItemType.values();
        int ord = itemType.ordinal();
        //last one
        if(ord == types.length - 1){
            return types[0];
        }else{
            return types[ord + 1];
        }
    }

}
