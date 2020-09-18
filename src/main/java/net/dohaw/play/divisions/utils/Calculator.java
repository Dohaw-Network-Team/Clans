package net.dohaw.play.divisions.utils;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.files.DefaultConfig;

import java.util.EnumMap;


public class Calculator {

    private static DefaultConfig config;

    public static double factorInDamage(PlayerData data, double dmg, boolean isBowDamage){
        EnumMap<Stat, Double> skills = data.getStatLevels();
        double damageEnhancer;
        double dmgScale;
        double dmgDivisionScale;
        if(isBowDamage){
            dmgDivisionScale = config.getRangedDamageDivisionScale();
            dmgScale = config.getBowDamageScale();
            damageEnhancer = skills.get(Stat.PIERCING);
        }else{
            dmgDivisionScale = config.getMeleeDamageDivisionScale();
            dmgScale = config.getMeleeDamageScale();
            damageEnhancer = skills.get(Stat.STRENGTH);
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

    public static void setDefaultConfig(DefaultConfig defaultConfig){
        config = defaultConfig;
    }


}
