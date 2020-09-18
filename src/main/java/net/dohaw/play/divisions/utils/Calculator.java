package net.dohaw.play.divisions.utils;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.files.DefaultConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.EnumMap;


public class Calculator {

    private static DefaultConfig config;

    public static double factorInDamage(PlayerData data, double dmg, boolean isBowDamage){
        double damageEnhancer;
        double dmgScale;
        double dmgDivisionScale;
        if(isBowDamage){
            dmgDivisionScale = config.getRangedDamageDivisionScale();
            dmgScale = config.getBowDamageScale();
            damageEnhancer = getTotalStat(data, Stat.PIERCING);
        }else{
            dmgDivisionScale = config.getMeleeDamageDivisionScale();
            dmgScale = config.getMeleeDamageScale();
            damageEnhancer = getTotalStat(data, Stat.STRENGTH);
        }
        return (dmg + (Math.pow(damageEnhancer, dmgScale))) / dmgDivisionScale;
    }

    public static double factorInToughness(PlayerData data, double dmg){
        EnumMap<Stat, Double> skills = data.getStatLevels();
        double toughnessLvl = skills.get(Stat.FORTITUDE);
        return dmg - (Math.pow(toughnessLvl, config.getToughnessScale()));
    }

    public static double getStatFromItem(double rawStatOnItem){
        return (rawStatOnItem / config.getRawStatDivisionScale()) / config.getRawStatDivisionTotalityScale();
    }

    public static double calculateMaxRegen(PlayerData pd){
        return (config.getBaseRegen() + (Math.pow(pd.getLevel(), 6) + getTotalStat(pd, Stat.RESTORATION)));
    }

    public static double calculateRegen(PlayerData pd){
        double restoLevel = pd.getStatLevels().get(Stat.RESTORATION);
        if(restoLevel == 1){
            return config.getRegenIncrement();
        }else{
            return Math.pow(restoLevel, 2) + config.getRegenIncrement();
        }
    }

    public static double getTotalStat(PlayerData data, Stat stat){

        double statLevelValue = data.getStatLevels().get(stat);
        Player player = data.getPLAYER().getPlayer();
        PlayerInventory playerInventory = player.getInventory();

        double totalStatValue = statLevelValue;
        for(ItemStack stack : playerInventory.getContents()){
            totalStatValue += getStatFromItem(CustomItem.getCustomItemStat(stack, stat));
        }

        return totalStatValue;
    }

    public static void setDefaultConfig(DefaultConfig defaultConfig){
        config = defaultConfig;
    }


}
