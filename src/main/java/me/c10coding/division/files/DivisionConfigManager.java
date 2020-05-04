package me.c10coding.division.files;

import com.google.inject.Inject;
import me.c10coding.coreapi.files.ConfigManager;
import me.c10coding.division.DivisionPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class DivisionConfigManager extends ConfigManager {

    private DivisionPlugin plugin;
    private Economy e;

    @Inject
    public DivisionConfigManager(DivisionPlugin plugin) {
        super(plugin, "divisions.yml");
        e = plugin.getEconomy();
    }

    @Override
    public void validateConfigs() {
        File[] files = {new File(plugin.getDataFolder(), "divisions.yml"), new File(plugin.getDataFolder(), "config.yml")};
        for(File f : files){
            if(!f.exists()) {
                plugin.saveResource(f.getName(), false);
                Bukkit.getConsoleSender().sendMessage(this.plugin.getPrefix() + " Loading " + f.getName());
            }
        }
    }

    private boolean isDivisionPublic(String divisionName){
        return config.getBoolean("Divisions." + divisionName + ".Status");
    }

    private List<String> getDivisionList(){
        return getList("ListOfDivisions");
    }

    private boolean inSameDivision(UUID player1, UUID player2){
        return getPlayerDivision(player1).equals(getPlayerDivision(player2));
    }

    private void createDivision(String clanName, Player owner, boolean isPublic){ }

    private boolean isDivision(){
        return false;
    }

    private String getPlayerDivision(UUID uuid){
        return null;
    }

    private boolean isOwner(){
        return false;
    }

    private void removeDivision(){}

    private void addMemberToDivision(){}

    private void removeMemberFromDivision(){}

    private void changeDivisionStatus(){}

}
