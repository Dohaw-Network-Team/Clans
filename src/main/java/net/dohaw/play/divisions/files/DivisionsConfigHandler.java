package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.helpers.EnumHelper;
import net.dohaw.play.divisions.Division;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DivisionsConfigHandler{

    private DivisionsListConfig dlc;
    private DivisionsPlugin plugin;
    private EnumHelper enumHelper;
    private PlayerDataManager playerDataManager;
    private PlayerDataHandler playerDataHandler;
    private Economy e;

    public DivisionsConfigHandler(DivisionsPlugin plugin) {
        this.dlc = new DivisionsListConfig(plugin);
        this.plugin = plugin;
        this.enumHelper = plugin.getCoreAPI().getEnumHelper();
        this.e = DivisionsPlugin.getEconomy();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.playerDataHandler = new PlayerDataHandler(plugin);
    }

    public HashMap<String, Division> loadDivisions(){

        HashMap<String, Division> divisionsMap = new HashMap<>();
        int amountLoaded = 0;
        for(String divisionName : dlc.getListOfDivisions()){
            File divisionFile = new File(plugin.getDataFolder() + File.separator + "/divisionsData", divisionName + ".yml");
            if(divisionFile.exists()){
                YamlConfiguration divisionConfig = YamlConfiguration.loadConfiguration(divisionFile);
                Division loadedDivision = loadDivision(divisionName, divisionConfig);
                divisionsMap.put(divisionName, loadedDivision);
                amountLoaded++;
            }
        }

        plugin.getLogger().info("Loaded " + amountLoaded + " divisions into memory");
        return divisionsMap;
    }

    public Division loadDivision(String divisionName, FileConfiguration divisionConfig){

        List<String> memberUUIDStrings = divisionConfig.getStringList("Members");
        List<PlayerData> members = new ArrayList<>();
        UUID leaderUUID = UUID.fromString(divisionConfig.getString("Leader"));
        PlayerData leaderPlayerData = playerDataHandler.loadPlayerData(leaderUUID);
        Division division = new Division(divisionName, divisionConfig, leaderPlayerData);

        /*
            PLAYER DATA MANAGER WILL LOAD ONLY ONLINE PLAYERS, BUT DIVISIONS HOLD OFFLINE PLAYER DATA AS WELL SO WE LOAD OFFLINE PLAYERS HERE.
         */
        for(String memberStringUUID : memberUUIDStrings){
            UUID memberUUID = UUID.fromString(memberStringUUID);
            PlayerData playerData = playerDataHandler.loadPlayerData(memberUUID);
            members.add(playerData);
        }

        division.setPlayers(members);

        int divisionGold = (int) e.bankBalance(divisionName + "_Bank").balance;
        division.setGoldAmount(divisionGold);

        int power = divisionConfig.getInt("Power");
        division.setPower(power);

        division.setBankName(divisionName + "_Bank");

        int kills = divisionConfig.getInt("Kills");
        int casualties = divisionConfig.getInt("Casualties");
        division.setKills(kills);
        division.setCasualties(casualties);

        int shrinesConquered = divisionConfig.getInt("Shrines Conquered");
        division.setShrinesConquered(shrinesConquered);

        double heartsDestroyed = divisionConfig.getDouble("Hearts Destroyed");
        division.setHeartsDestroyed(heartsDestroyed);

                /*
                    Loads the specific permissions for a division
                 */
        EnumMap<Rank, EnumMap<Permission, Object>> rankPermissions = new EnumMap<>(Rank.class);
        for(Map.Entry<Rank, EnumMap<Permission, Object>> rank : rankPermissions.entrySet()){
            String rankString = enumHelper.enumToName(rank.getKey());
            EnumMap<Permission, Object> permissionMap = new EnumMap<>(Permission.class);
            for(Map.Entry<Permission, Object> perm : permissionMap.entrySet()){
                String permString = enumHelper.enumToName(perm.getKey());
                if(divisionConfig.get("Rank Permissions." + rankString + "." + permString) != null){
                    permissionMap.put(perm.getKey(), divisionConfig.get("Rank Permissions." + rankString + "." + permString));
                }
            }
            rankPermissions.put(rank.getKey(), permissionMap);
        }
        division.setRankPermissions(rankPermissions);
        return division;
    }

    public void saveDivisionsData(HashMap<String, Division> divisions){
        for(Map.Entry<String, Division> division : divisions.entrySet()){
            Division div = division.getValue();
            saveDivisionData(div);
        }
    }

    public void saveDivisionData(Division div){

        FileConfiguration divConfig = div.getConfig();
        List<PlayerData> members = div.getPlayers();
        List<String> memberUUIDs = new ArrayList<>();
        members.forEach(data -> memberUUIDs.add(data.getPlayerUUID().toString()));

        divConfig.set("Members", memberUUIDs);
        divConfig.set("Leader", div.getLeader().getPlayerUUID().toString());
        divConfig.set("Power", div.getPower());
        divConfig.set("Kills", div.getKills());
        divConfig.set("Casualties", div.getCasualties());
        divConfig.set("Shrines Conquered", div.getShrinesConquered());
        divConfig.set("Hearts Destroyed", div.getHeartsDestroyed());

        EnumMap<Rank, EnumMap<Permission, Object>> rankPermissions = div.getRankPermissions();
        for(Map.Entry<Rank, EnumMap<Permission, Object>> rank : rankPermissions.entrySet()){
            String rankString = enumHelper.enumToName(rank.getKey());
            EnumMap<Permission, Object> permissionMap = rank.getValue();
            for(Map.Entry<Permission, Object> perm : permissionMap.entrySet()){
                String permString = enumHelper.enumToName(perm.getKey());
                divConfig.set("Rank Permissions." + rankString + "." + permString, perm.getValue());
            }
        }

        File divFile = new File(plugin.getDataFolder() + File.separator + "/divisionsData", div.getName() + ".yml");

        try {
            divConfig.save(divFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public FileConfiguration getDivisionConfig(String divisionName){
        File playerFile = new File(plugin.getDataFolder() + File.separator + "/divisionsData", divisionName + ".yml");
        if(playerFile.exists()){
            return YamlConfiguration.loadConfiguration(playerFile);
        }

        try {
            playerFile.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return YamlConfiguration.loadConfiguration(playerFile);
    }

    public FileConfiguration createDivisionsConfig(String divisionName, UUID creatorUUID){

        FileConfiguration config = getDivisionConfig(divisionName);
        List<String> members = new ArrayList<>();
        members.add(creatorUUID.toString());
        config.set("Members", members);

        config.set("Leader", creatorUUID.toString());
        config.set("Power", 0);
        config.set("Kills", 0);
        config.set("Casualties", 0);
        config.set("Shrines Conquered", 0);
        config.set("Hearts Destroyed", 0);

        /*
            Puts in the default permissions from the DefaultPermConfig class.
         */
        EnumMap<Rank, EnumMap<Permission, Object>> defaultPerms = plugin.getDefaultPermConfig().getDefaultPerms();

        for(Map.Entry<Rank, EnumMap<Permission, Object>> rank : defaultPerms.entrySet()){
            String rankString = enumHelper.enumToName(rank.getKey());
            EnumMap<Permission, Object> permissionMap = rank.getValue();
            for(Map.Entry<Permission, Object> perm : permissionMap.entrySet()){
                String permString = enumHelper.enumToName(perm.getKey());
                config.set("Rank Permissions." + rankString + "." + permString, perm.getValue());
            }
        }

        File divisionFile = new File(plugin.getDataFolder() + File.separator + "/divisionsData", divisionName + ".yml");

        try {
            config.save(divisionFile);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        /*
            If there was an error with the creation of the file, it'll just return null
         */
        if(divisionFile.exists()){
            return config;
        }
        return null;
    }

    public void setPlayerDataManager(PlayerDataManager playerDataManager){
        this.playerDataManager = playerDataManager;
    }



}
