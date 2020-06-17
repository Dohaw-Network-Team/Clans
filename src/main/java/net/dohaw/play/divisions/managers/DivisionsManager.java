package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.Division;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.files.DivisionsConfigHandler;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class DivisionsManager implements Manager {

    private HashMap<String, Division> divisions;
    private DivisionsPlugin plugin;
    private DivisionsConfigHandler dch;
    private Economy e;

    public DivisionsManager(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.dch = new DivisionsConfigHandler(plugin);
        this.e = DivisionsPlugin.getEconomy();
    }

    @Override
    public Object getContents() {
        return divisions;
    }

    @Override
    public boolean hasContent(Object obj) {
        return divisions.get(obj) != null;
    }

    @Override
    public void saveContents() {
        dch.saveDivisionsData(divisions);
    }

    @Override
    public void loadContents() {
        this.divisions = dch.loadDivisions();
    }

    @Override
    public boolean addContent(Object content) {
        return false;
    }

    public boolean addContent(String divisionName, Object content){
        return divisions.put(divisionName, (Division) content) != null;
    }

    public void createNewDivision(String divisionName, Player creator){
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();

        PlayerData creatorData = playerDataManager.getByPlayerObj(creator);
        FileConfiguration newDivisionConfig = dch.createDivisionsConfig(divisionName, creator.getUniqueId());
        divisions.put(divisionName, new Division(divisionName, newDivisionConfig, creatorData));
        e.createBank(divisionName + "_Bank", Bukkit.getOfflinePlayer(creator.getUniqueId()));
    }

    @Override
    public boolean removeContent(Object content) {
        return false;
    }

    public Division getDivision(String divisionName){
        return divisions.get(divisionName);
    }

    public Division getByLeader(UUID leaderUUID){
        for(Map.Entry<String, Division> division : divisions.entrySet()){
            Division div = division.getValue();
            if(div.getLeader().getPlayerUUID().equals(leaderUUID)){
                return div;
            }
        }
        return null;
    }

}
