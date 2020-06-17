package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DivisionListConfig extends ConfigManager {

    public DivisionListConfig(JavaPlugin plugin) {
        super(plugin, "divisionsList.yml");
    }

    public List<String> getListOfDivisions(){
        return config.getStringList("List Of Divisions");
    }

    public void saveToFile(List<String> listOfDivisions){
        config.set("List Of Divisions", listOfDivisions);
        saveConfig();
    }

}
