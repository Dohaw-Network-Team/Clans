package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.helpers.EnumHelper;
import net.dohaw.play.archetypes.archetype.Stat;
import net.dohaw.play.divisions.DivisionsPlugin;
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
import java.util.*;

public class PlayerDataHandler {

    private DivisionsPlugin plugin;
    private EnumHelper enumHelper;

    private DefaultPermConfig defaultPermConfig;
    private Economy e;

    /*
        Pretty much just gets the permissions that are specific to a player in a division
     */
    public PlayerDataHandler(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.enumHelper = plugin.getAPI().getEnumHelper();
        this.e = DivisionsPlugin.getEconomy();
        this.defaultPermConfig = plugin.getDefaultPermConfig();
    }

    public PlayerData loadPlayerPermissions(FileConfiguration playerConfig, PlayerData data){

        for(Permission perm : Permission.values()){
            String key = enumHelper.enumToName(perm);
            if(playerConfig.get("Permissions." + key) != null){
                Object value = playerConfig.get("Permissions." + key);
                data.putPermission(perm, value);
            }else{
                /*
                    If the permissions was just recently added via hardcode in the Permissions enum, it will give them the default value from the Fresh Meat Rank. When the permissions is saved, it will add the permission to the list of permissions in the .yml file.
                 */
                EnumMap<Permission, Object> freshMeatPerms = defaultPermConfig.getDefaultRankPermissions(Rank.FRESH_MEAT);
                data.putPermission(perm, freshMeatPerms.get(perm));
            }
        }
        return data;
    }

    public PlayerData loadPlayerAttributes(FileConfiguration playerConfig, PlayerData data){
        EnumMap<Stat, Double> stats = new EnumMap<>(Stat.class);
        for(Stat stat : Stat.values()){
            String statKey = enumHelper.enumToName(stat);
            double statLevel;
            if(playerConfig.get("Stats.Attributes." + statKey) != null){
                statLevel = playerConfig.getDouble("Stats.Attributes." + statKey);
            }else{
                statLevel = 1;
            }
            stats.put(stat, statLevel);
        }
        data.setStatLevels(stats);

        double mana = playerConfig.getDouble("Mana");
        data.setMana(mana);

        return data;
    }

    public PlayerData loadPlayerStats(FileConfiguration playerConfig, PlayerData data){

        int kills = playerConfig.getInt("Stats.Kills");
        int casualties = playerConfig.getInt("Stats.Casualties");
        int shrinesConquered = playerConfig.getInt("Stats.Shrines Conquered");
        double heartsDestroyed = playerConfig.getInt("Stats.Hearts Destroyed");

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

        File playerFile = new File(plugin.getDataFolder() + File.separator + "/playerData", playerData.getPLAYER_UUID().toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        double heartsDestroyed = playerData.getHeartsDestroyed();
        int kills = playerData.getKills();
        int casualties = playerData.getCasualties();
        int shrinesConquered = playerData.getShrinesConquered();

        /*
            If they aren't in a division, then leave it as null. If they aren't in a division, then they can't have a rank
         */
        if(playerData.getDivision() != null){
            String divisionName = playerData.getDivision();
            config.set("Division", divisionName);
            if(playerData.getRank() != null){
                String divisionRank = enumHelper.enumToName(playerData.getRank());
                config.set("DivisionRank", divisionRank);
            }
        }else{
            config.set("Division", "none");
            config.set("DivisionRank", "none");
        }

        config.set("Stats.Hearts Destroyed", heartsDestroyed);
        config.set("Stats.Kills", kills);
        config.set("Stats.Casualties", casualties);
        config.set("Stats.Shrines Conquered", shrinesConquered);

        EnumMap<Permission, Object> playerPermission = playerData.getPermissions();
        for(Map.Entry<Permission, Object> permEntry : playerPermission.entrySet()){
            String key = enumHelper.enumToName(permEntry.getKey());
            Object value = permEntry.getValue();
            config.set("Permissions." + key, value);
        }

        /*
            ARCHETYPE STUFF AFTER HERE.
         */
        if(playerData.getArchetype() != null){
            EnumMap<Stat, Double> stats = playerData.getStatLevels();
            for(Map.Entry<Stat, Double> statEntry : stats.entrySet()){
                String key = enumHelper.enumToName(statEntry.getKey());
                double value = statEntry.getValue();
                config.set("Stats.Attributes." + key, value);
            }

            double mana = playerData.getMana();
            config.set("Mana", mana);
        }

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

        /*
        if(!playerDataConfig.getString("Division").equalsIgnoreCase("none")){
            Division division = null;
            data.setPlayerDivision(division.getName());
        }*/

        data = loadPlayerPermissions(playerDataConfig, data);
        data = loadPlayerStats(playerDataConfig, data);
        if(data.getArchetype() != null){
            data = loadPlayerAttributes(playerDataConfig, data);
        }
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
