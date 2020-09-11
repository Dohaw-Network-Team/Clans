package net.dohaw.play.divisions.customitems;

import lombok.Getter;
import org.bukkit.Material;

public enum Rarity {

    EXOTIC(Material.DIAMOND),
    EPIC(Material.IRON_ORE),
    RARE(Material.STONE),
    UNCOMMON(Material.OAK_PLANKS),
    COMMON(Material.GRASS_BLOCK);

    @Getter
    private Material menuMat;
    Rarity(Material menuMat){
        this.menuMat = menuMat;
    }

    public static Rarity getNextItemType(Rarity rarity){
        Rarity[] types = Rarity.values();
        int ord = rarity.ordinal();
        //last one
        if(ord == types.length - 1){
            return types[0];
        }else{
            return types[ord + 1];
        }
    }

}
