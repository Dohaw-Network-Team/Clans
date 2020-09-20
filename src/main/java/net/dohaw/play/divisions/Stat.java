package net.dohaw.play.divisions;

import lombok.Getter;
import org.bukkit.Material;

import java.util.EnumMap;

public enum Stat {

    STRENGTH(Material.GOLDEN_SWORD, 10),
    SPELL_POWER(Material.BLAZE_POWDER, 12),
    FORTITUDE(Material.SHIELD, 14),
    MITIGATION(Material.EMERALD, 16),
    QUICKNESS(Material.GHAST_TEAR, 28),
    STEALTHINESS(Material.WITHER_ROSE, 30),
    ACCURACY(Material.BOW, 32),
    LUCK(Material.EXPERIENCE_BOTTLE, 34),
    PIERCING(Material.ARROW, 38),
    MAX_HEALTH(Material.PORKCHOP, 42),
    RESTORATION(Material.GREEN_DYE, 44);

    @Getter private Material menuMat;
    @Getter private int menuSlot;
    Stat(Material menuMat, int menuSlot){
        this.menuMat = menuMat;
        this.menuSlot = menuSlot;
    }

    public static Stat getStatByMenuMat(Material mat){
        for(Stat stat : Stat.values()){
            if(stat.menuMat == mat){
                return stat;
            }
        }
        return null;
    }

    public static EnumMap<Stat, Double> getDefaultStats(){
        EnumMap<Stat, Double> defaultStats = new EnumMap<Stat, Double>(Stat.class);
        for(Stat stat : Stat.values()){
            defaultStats.put(stat, 1.0);
        }
        return defaultStats;
    }

}
