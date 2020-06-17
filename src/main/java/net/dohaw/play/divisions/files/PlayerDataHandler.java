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
        double heartsDestroyed = config.getInt("Stats.Hearts Destroyed");

        data.setKills(kills);
        data.setCasualties(casualties);
        data.setShrinesConquered(shrinesConquered);
        data.setHeartsDestroyed(heartsDestroyed);
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
        for(PlayerData data : playerDataList){
            saveData(data);
        }
    }

    /*
        Saves player data when they leave the server
     */
    public void saveData(PlayerData playerData){

        File playerFile = new File(plugin.getDataFolder() + File.separator + "/playerData", playerData.getPlayerUUID().toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        double heartsDestroyed = playerData.getHeartsDestroyed();
        int kills = playerData.getKills();
        int casualties = playerData.getCasualties();
        int shrinesConquered = playerData.getShrinesConquered();

        /*
            If they aren't in a division, then leave it as null. If they aren't in a division, then they can't have a rank
         */
        if(playerData.getDivision() != null){
            String divisionName = playerData.getDivision().getName();
            config.set("Division", divisionName);

            if(playerData.getRank() != null){
                String divisionRank = enumHelper.enumToName(playerData.getRank());
                config.set("DivisionRank", divisionRank);
            }
        }

        config.set("Stats.Hearts Destroyed", heartsDestroyed);
        config.set("Stats.Kills", kills);
        config.set("Stats.Casualties", casualties);
        config.set("Stats.Shrines Conquered", shrinesConquered);

        try {
            config.save(playerFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /*
        Loads a specific player's data
     */
    public PlayerData loadPlayerData(UUID uuid){

        FileConfiguration playerDataConfig = getPlayerDataConfig(uuid);
        PlayerData data;

        if(playerDataConfig.getString("DivisionRank").equalsIgnoreCase("none")){
            data = new PlayerData(Bukkit.getOfflinePlayer(uuid), getPlayerDataConfig(uuid), null);
        }else{
            data = new PlayerData(Bukkit.getOfflinePlayer(uuid), getPlayerDataConfig(uuid), (Rank) enumHelper.nameToEnum(Rank.class, playerDataConfig.getString("DivisionRank")));
        }

        if(!playerDataConfig.getString("Division").equalsIgnoreCase("none")){
            plugin.getLogger().info(divisionsManager.getContents().toString());
            plugin.getLogger().info(playerDataConfig.getString("Division"));
            Division division = divisionsManager.getDivision(playerDataConfig.getString("Division"));
            data.setPlayerDivision(division);
        }

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

        config.set("Division", "none");
        config.set("DivisionRank", "none");
        config.set("Stats.Hearts Destroyed", 0);
        config.set("Stats.Kills", 0);
        config.set("Stats.Casualties", 0);
        config.set("Stats.Shrines Conquered", 0);

        try {
            config.save(playerFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public boolean hasPermissionData(UUID uuid){
        return getPlayerDataConfig(uuid).getConfigurationSection("Permissions") != null;
    }

    public FileConfiguration getPlayerDataConfig(UUID uuid){
        File playerFile = new File(plugin.getDataFolder() + File.separator + "/playerData", uuid.toString() + ".yml");
        if(!playerFile.exists()){
            createPlayerFile(uuid);
        }
        return YamlConfiguration.loadConfiguration(playerFile);
    }

}
