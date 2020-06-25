package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DivisionsListConfig extends ConfigManager {

    public DivisionsListConfig(JavaPlugin plugin) {
        super(plugin, "divisionsList.yml");
    }

    public List<String> getListOfDivisions(){
        return config.getStringList("List of Divisions");
    }

    public void saveToFile(List<String> listOfDivisions){
        config.set("List Of Divisions", listOfDivisions);
        saveConfig();
    }

    public void addDivision(String divisionName){
        List<String> divisionNames = getListOfDivisions();
        divisionNames.add(divisionName);
        config.set("List of Divisions", divisionNames);
        saveConfig();
    }

    public void removeDivision(String divisionName){
        List<String> divisionNames = getListOfDivisions();
        divisionNames.remove(divisionName);
        config.set("List of Divisions", divisionNames);
        saveConfig();
    }
}
