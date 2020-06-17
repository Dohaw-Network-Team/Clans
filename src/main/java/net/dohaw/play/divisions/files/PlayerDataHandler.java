package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.helpers.EnumHelper;
import net.dohaw.play.divisions.Division;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataHandler {

    private DivisionsPlugin plugin;
    private DivisionsManager divisionsManager;
    private EnumHelper enumHelper;
    private Economy e;

    /*
        Pretty much just gets the permissions that are specific to a player in a division
     */
    public PlayerDataHandler(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.enumHelper = plugin.getCoreAPI().getEnumHelper();
        this.divisionsManager = plugin.getDivisionsManager();
        this.e = DivisionsPlugin.getEconomy();
    }

    public PlayerData loadPlayerPermissions(FileConfiguration playerConfig, PlayerData data){
        if(hasPermissionData(data.getPlayerUUID())){
            for(Permission perm : Permission.values()){
                String key = enumHelper.enumToName(perm);
                if(playerConfig.get("Permissions." + key) != null){
                    Object value = playerConfig.get("Permissions." + key);
                    data.setPermission(perm, value);
                }
            }
        }
        return data;
    }

    public PlayerData loadPlayerStats(FileConfiguration config, PlayerData data){

        int kills = config.getInt("Stats.Kills");
        int casualties = config.getInt("Stats.Casualties");
        int shrinesConquered = config.getInt("Stats.Shrines Conquered");

        data.setKills(kills);
        data.setCasualties(casualties);
        data.setShrinesConquered(shrinesConquered);
        return data;
    }

    /*
        Loads all player data
     */
    public List<PlayerData> loadData(){
        List<PlayerData> playerData = new ArrayList<>();
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            playerData.add(loadPlayerData(player.getUniqueId()));
        }
        return playerData;
    }

    public void saveData(List<PlayerData> playerDataList){

    }

    public void saveData(PlayerData playerData){

    }

    /*
        Loads a specific player's data
     */
    public PlayerData loadPlayerData(UUID uuid){
        FileConfiguration playerDataConfig = getPlayerDataConfig(uuid);
        PlayerData data = new PlayerData(Bukkit.getOfflinePlayer(uuid), getPlayerDataConfig(uuid), (Rank) enumHelper.nameToEnum(Rank.class, playerDataConfig.getString("DivisionRank")));

        String divisionName = playerDataConfig.getString("Division");
        Division division = divisionsManager.getDivision(divisionName);

        data.setPlayerDivision(division);
        data = loadPlayerPermissions(playerDataConfig, data);
        data = loadPlayerStats(playerDataConfig, data);
        return data;
    }

    public boolean hasPlayerData(UUID uuid){
        File playerFile = new File(plugin.getDataFolder() + File.separator + "/playerData", uuid.toString() + ".yml");
        return playerFile.exists();
    }

    public void createPlayerFile(UUID uuid){

        File playerFile = new File(plugin.getDataFolder() + File.separator + "/playerData", uuid.toString() + ".yml");
        try {
            playerFile.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        config.set("Stats.Hearts Destroyed", 0);
        config.set("Stats.Kills", 0);
        config.set("Stats.Casualties", 0);
        config.set("Stats.Division", "None");
        config.set("Stats.Shrines Conquered", 0);

    }

    public boolean hasPermissionData(UUID uuid){
        return !getPlayerDataConfig(uuid).getConfigurationSection("Permissions").getKeys(false).isEmpty();
    }

    public FileConfiguration getPlayerDataConfig(UUID uuid){
        File playerFile = new File(plugin.getDataFolder() + File.separator + "/playerData", uuid.toString() + ".yml");
        if(playerFile.exists()){
            return YamlConfiguration.loadConfiguration(playerFile);
        }
        createPlayerFile(uuid);
        return YamlConfiguration.loadConfiguration(playerFile);
    }

}
