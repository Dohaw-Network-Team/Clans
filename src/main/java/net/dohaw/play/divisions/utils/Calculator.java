package net.dohaw.play.divisions.utils;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.files.DefaultConfig;

import java.util.EnumMap;


public class Calculator {

    private static DefaultConfig defaultConfig;

    public static double factorInDamage(PlayerData data, double dmg, boolean isBowDamage){
        EnumMap<Stat, Double> skills = data.getStatLevels();
        double damageEnhancer;
        if(isBowDamage){
            damageEnhancer = skills.get(Stat.PIERCING);
        }else{
            damageEnhancer = skills.get(Stat.STRENGTH);
        }
        return (dmg + (Math.pow(damageEnhancer, defaultConfig.getMeleeDamageScale()))) / defaultConfig.getMeleeDamageDivisionScale();
    }

    public static double factorInToughness(PlayerData data, double dmg){
        EnumMap<Stat, Double> skills = data.getStatLevels();
        double toughnessLvl = skills.get(Stat.FORTITUDE);
        return dmg - (Math.pow(toughnessLvl, defaultConfig.getToughnessScale()));
    }

    public static double getStatFromItem(double rawStatOnItem){
        return (rawStatOnItem / defaultConfig.getRawStatDivisionScale()) / defaultConfig.getRawStatDivisionTotalityScale();
    }

    public static void setDefaultConfig(DefaultConfig defaultConfig){
        defaultConfig = defaultConfig;
    }


}
