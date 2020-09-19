package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig extends Config {

    public DefaultConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public int getDefaultDivisionGoldAmount(){
        return config.getInt("Default Division Gold Amount");
    }

    public double getMeleeDamageScale(){
        return config.getDouble("Damage Calculations.Scale.Melee Damage Scale");
    }

    public double getToughnessScale(){
        return config.getDouble("Damage Calculations.Scale.Toughness Scale");
    }

    public double getBowDamageScale(){
        return config.getDouble("Damage Calculations.Scale.Bow Damage Scale");
    }

    public double getMeleeDamageDivisionScale(){
        return config.getDouble("Damage Calculations.Division Scale.Melee Damage Division Scale");
    }

    public double getRangedDamageDivisionScale(){
        return config.getDouble("Damage Calculations.Division Scale.Ranged Damage Division Scale");
    }

    public double getRawStatDivisionScale(){
        return config.getDouble("Raw Stat Division Scale");
    }

    public double getRawStatDivisionTotalityScale(){
        return config.getDouble("Raw Stat Division Totality Scale");
    }

    public double getBaseRegenerationAmount(){
        return config.getDouble("Base Regeneration Amount");
    }

    public double getBaseRegenerationMax(){
        return config.getDouble("Base Regeneration Max");
    }

    public long getBaseRegenerationInterval(){
        return (long) config.getDouble("Base Regeneration Interval");
    }

}
