package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.division.DivisionStatus;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.files.DivisionsConfigHandler;
import net.dohaw.play.divisions.files.DivisionsListConfig;
import net.dohaw.play.divisions.PlayerData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DivisionsManager implements Manager {

    private HashMap<String, Division> divisions = new HashMap<>();
    private DivisionsPlugin plugin;
    private DivisionsListConfig divisionsListConfig;
    private Economy e;

    public DivisionsManager(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.e = DivisionsPlugin.getEconomy();
        this.divisionsListConfig = new DivisionsListConfig(plugin);
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
        DivisionsConfigHandler dch = new DivisionsConfigHandler(plugin);
        dch.saveDivisionsData(divisions);
    }

    @Override
    public void loadContents() {
        DivisionsConfigHandler dch = new DivisionsConfigHandler(plugin);
        this.divisions = dch.loadDivisions();
    }

    @Override
    public boolean addContent(Object content) {
        return false;
    }

    public boolean addContent(String divisionName, Object content){
        return divisions.put(divisionName, (Division) content) != null;
    }

    public void createNewDivision(String divisionName, Player creator, DivisionStatus status){
        DivisionsConfigHandler dch = new DivisionsConfigHandler(plugin);
        FileConfiguration newDivisionConfig = dch.createDivisionsConfig(divisionName, creator.getUniqueId(), status);
        Division newDivision = dch.loadDivision(divisionName, newDivisionConfig, status);

        divisions.put(divisionName, newDivision);
        e.createBank(divisionName + "_Bank", Bukkit.getOfflinePlayer(creator.getUniqueId()));
        divisionsListConfig.addDivision(divisionName);
    }

    @Override
    public boolean removeContent(Object content) {
        return false;
    }

    public Division getDivision(String divisionName){
        return divisions.get(divisionName);
    }

    public void updateDivision(String divisionName, Division division){
        divisions.replace(divisionName, division);
    }

    public Division getByLeader(UUID leaderUUID){
        for(Map.Entry<String, Division> division : divisions.entrySet()){
            Division div = division.getValue();
            if(div.getLeader().getPLAYER_UUID().equals(leaderUUID)){
                return div;
            }
        }
        return null;
    }

    public void disbandDivision(String divisionName){

        Division division = getDivision(divisionName);
        divisions.remove(divisionName);
        divisionsListConfig.removeDivision(divisionName);

        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        for(UUID uuid : division.getPlayers()){
            PlayerData playerData = playerDataManager.getPlayerByUUID(uuid);

            playerData.setDivision(null);
            playerData.setRank(null);
            playerDataManager.updatePlayerData(playerData.getPLAYER_UUID(), playerData);
        }

        DivisionsConfigHandler dch = new DivisionsConfigHandler(plugin);
        dch.deleteDivisionConfig(divisionName);

    }

}
