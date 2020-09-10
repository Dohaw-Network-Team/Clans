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

}
