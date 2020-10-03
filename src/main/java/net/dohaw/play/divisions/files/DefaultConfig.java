package net.dohaw.play.divisions.files;

import net.dohaw.play.corelib.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig extends Config {

    public final String CALCULATIONS_PATH = "Calculations";
    public final String SPELL_CALCULATIONS_PATH = "Calculations.Spells";
    public final String SPELL_PROPERTIES_PATH = "Spells";

    public DefaultConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public int getDefaultDivisionGoldAmount(){
        return config.getInt("Default Division Gold Amount");
    }

    public double getMeleeDamageScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Scale.Melee Damage Scale");
    }

    public double getToughnessScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Scale.Toughness Scale");
    }

    public double getBowDamageScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Scale.Bow Damage Scale");
    }

    public double getMeleeDamageDivisionScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Division Scale.Melee Damage Division Scale");
    }

    public double getRangedDamageDivisionScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Division Scale.Ranged Damage Division Scale");
    }

    public double getRawStatDivisionScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Division Scale.Raw Stat Division Scale");
    }

    public double getRawStatDivisionTotalityScale(){
        return config.getDouble(CALCULATIONS_PATH + ".Division Scale.Raw Stat Division Totality Scale");
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

    /*
        How much each Restoration point reduces the percentage of the spell regen cost
     */
    public double getPercentageReductionPerRestoPoint(){
        return config.getDouble("Percentage Reduction Per Restoration Point");
    }

    /*
        The minimum percentage of regen that a spell should cost
     */
    public double getMinimumPercentageSpellCost(){
        return config.getDouble("Mininmum Regeneration Percentage");
    }

    public double getHeatUpLuckMultiple(){
        return config.getDouble(SPELL_CALCULATIONS_PATH + ".Heating Up.Total Luck Multiple");
    }

    public double getEscapeScale(){
        return config.getDouble(SPELL_CALCULATIONS_PATH + ".Escape.Velocity Scale");
    }

    public boolean isInDebugMode(){
        return config.getBoolean("Debug Mode");
    }

}
