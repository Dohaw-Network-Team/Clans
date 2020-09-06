package net.dohaw.play.divisions.utils;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;

import java.util.EnumMap;


public class DamageCalculator {

    public static double factorInDamage(PlayerData data, double dmg, double dmgScale, double divider){
        EnumMap<Stat, Double> skills = data.getStatLevels();
        double strengthLvl = skills.get(Stat.STRENGTH);
        return (dmg + (Math.pow(strengthLvl, dmgScale))) / divider;
    }

    public static double factorInToughness(PlayerData data, double dmg, double toughnessScale){
        EnumMap<Stat, Double> skills = data.getStatLevels();
        double toughnessLvl = skills.get(Stat.FORTITUDE);
        return dmg - (Math.pow(toughnessLvl, toughnessScale));
    }


}
