package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfig extends ConfigManager {

    public DefaultConfig(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    public int getDefaultDivisionGoldAmount(){
        return config.getInt("Default Division Gold Amount");
    }

}
