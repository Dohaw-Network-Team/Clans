package net.dohaw.play.divisions.utils;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.files.DefaultConfig;
import org.bukkit.Bukkit;
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
        return (config.getBaseRegenerationMax() + (Math.pow(pd.getLevel(), 6) + getTotalStat(pd, Stat.RESTORATION)));
    }

    public static double calculateRegen(PlayerData pd){
        double totalResto = getTotalStat(pd, Stat.RESTORATION);
        if(totalResto == 1){
            return config.getBaseRegenerationAmount();
        }else{
            return Math.pow(totalResto, 2) + config.getBaseRegenerationAmount();
        }
    }

    public static long calculateRegenInterval(PlayerData pd){
        double totalResto = getTotalStat(pd, Stat.RESTORATION);
        double baseRegenInterval = config.getBaseRegenerationInterval();
        if(totalResto != 0){
            return (long) (baseRegenInterval - (totalResto / 2.0));
        }else{
            return (long) baseRegenInterval;
        }
    }

    public static double getTotalStat(PlayerData data, Stat stat){

        double statLevelValue = data.getStatLevels().get(stat);
        Player player = data.getPlayer().getPlayer();
        PlayerInventory playerInventory = player.getInventory();

        double totalStatValue = statLevelValue;
        for(ItemStack stack : playerInventory.getContents()){
            if(stack != null){
                double rawCustomItemStat = CustomItem.getCustomItemStat(stack, stat);
                if(rawCustomItemStat != 0){
                    double customItemStat = getStatFromItem(rawCustomItemStat);
                    totalStatValue += customItemStat;
                }
            }
        }

        return totalStatValue;
    }

    /*
        Reduces the percentage of regen a spell costs based on your restoration levels
     */
    public static double getSpellPercentageRegenCost(PlayerData pd, double percentageOfRegen){

        double totalResto = getTotalStat(pd, Stat.RESTORATION);
        if(totalResto != 1){

            double reductionPerRestoPoint = config.getPercentageReductionPerRestoPoint();
            double reduction = percentageOfRegen - (reductionPerRestoPoint * totalResto);
            double minPercentageSpellCost = config.getMinimumPercentageSpellCost();
            if(reduction < minPercentageSpellCost){
                return minPercentageSpellCost;
            }else{
                return reduction;
            }

        }
        return percentageOfRegen;
    }

    public static double getSpellRegenCost(PlayerData pd, SpellWrapper spell){
        double maxRegen = Calculator.calculateMaxRegen(pd);
        double percentageRegenCost = getSpellPercentageRegenCost(pd, spell.getPercentageRegenAffected());
        double regenCost = maxRegen * percentageRegenCost;
        return regenCost;
    }

    public static double getCooldownTimeLeft(long cooldownEnd){
        return cooldownEnd - System.currentTimeMillis();
    }

    public static void setDefaultConfig(DefaultConfig defaultConfig){
        config = defaultConfig;
    }


}
