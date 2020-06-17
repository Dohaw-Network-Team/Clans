package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DivisionConfig extends ConfigManager {

    public DivisionConfig(JavaPlugin plugin, String fileName){
        super(plugin, fileName);
    }

}
