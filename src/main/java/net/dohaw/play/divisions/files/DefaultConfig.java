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
        return config.getDouble("Melee Damage Scale");
    }

    public double getToughnessScale(){
        return config.getDouble("Toughness Scale");
    }

    public double getMeleeDamageDivisionScale(){
        return config.getDouble("Melee Damage Division Scale");
    }

    public double getRangedDamageDivisionScale(){
        return config.getDouble("Ranged Damage Division Scale");
    }

    public double getBowDamageScale(){
        return config.getDouble("Bow Damage Scale");
    }

}
