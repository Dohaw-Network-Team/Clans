package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.events.custom.TemporaryPlayerDataCreationEvent;
import net.dohaw.play.divisions.PlayerDataHandler;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager implements Manager{

    private DivisionsPlugin plugin;
    private PlayerDataHandler playerDataHandler;
    private List<PlayerData> playerDataList = new ArrayList<>();

    public PlayerDataManager(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.playerDataHandler = new PlayerDataHandler(plugin);
    }

    @Override
    public Object getContents() {
        return playerDataList;
    }

    public PlayerData getPlayerByUUID(UUID playerUUID){
        for(PlayerData data : playerDataList){
            if(data.getPLAYER_UUID().equals(playerUUID)){
                return data;
            }
        }
        return null;
    }

    public PlayerData getByPlayerObj(Player player){
        for(PlayerData data : playerDataList){
            if(data.getPlayer().getPlayer().equals(player)){
                return data;
            }
        }
        return null;
    }

    public List<PlayerData> getByRank(Rank rank){
        List<PlayerData> playersWithRank = new ArrayList<>();
        for(PlayerData data : playerDataList){
            if(data.getRank().equals(rank)){
                playersWithRank.add(data);
            }
        }
        return null;
    }

    public List<PlayerData> getByDivisionName(String divisionName){
        List<PlayerData> membersData = new ArrayList<>();
        for(PlayerData data : playerDataList){
            if(data.getDivision().equalsIgnoreCase(divisionName)){
                membersData.add(data);
            }
        }
        return membersData;
    }

    @Override
    public boolean hasContent(Object obj) {
        return playerDataList.contains(obj);
    }

    @Override
    public void saveContents() {
        playerDataHandler.saveData(playerDataList);
    }

    @Override
    public void loadContents() {
        this.playerDataList = playerDataHandler.loadData();
        plugin.getLogger().info("Loaded " + playerDataList.size() + " player(s) into memory");
    }

    public PlayerData loadPlayerData(UUID uuid){
        return playerDataHandler.loadPlayerData(uuid);
    }

    @Override
    public boolean addContent(Object content) { return false; }

    public void addPlayerData(UUID u){
        playerDataList.add(playerDataHandler.loadPlayerData(u));
    }

    public void updatePlayerData(UUID playerUUID, PlayerData newData){
        /*
            It may invoke null if the newData value is temporarily loaded data.
         */
        PlayerData playerData = getPlayerByUUID(playerUUID);
        if(playerData == null){
            playerDataList.add(newData);
            Bukkit.getPluginManager().callEvent(new TemporaryPlayerDataCreationEvent(playerUUID));
        }else{
            playerDataList.set(playerDataList.indexOf(playerData), newData);
        }
    }

    public void removePlayerData(UUID u){
        PlayerData pd = getPlayerByUUID(u);
        pd.stopRegener();
        playerDataHandler.saveData(pd);
        playerDataList.remove(pd);
    }

    @Override
    public boolean removeContent(Object content) {
        return false;
    }

    public void setPlayerDivisions() {
        for(PlayerData data : playerDataList){
            setPlayerDivision(data);
        }
    }

    public void setPlayerDivision(PlayerData data){
        FileConfiguration playerDataConfig = data.getPLAYER_CONFIG();
        if(!playerDataConfig.getString("Division").equalsIgnoreCase("none")){
            data.setDivision((playerDataConfig.getString("Division")));
        }
    }

    /*
        Checks if player has permission
     */
    public boolean can(UUID u, Permission permission){

        PlayerData data = getPlayerByUUID(u);
        Division division = plugin.getDivisionsManager().getDivision(data.getDivision());
        Rank rank = data.getRank();

        /*
            This means that they are leader of the division
         */
        if(rank == null){
            return true;
        }

        EnumMap<Rank, EnumMap<Permission, Object>> divisionRankPermissions = division.getRankPermissions();
        EnumMap<Permission, Object> rankPermissions = divisionRankPermissions.get(rank);
        boolean rankCan = (boolean) rankPermissions.get(permission);

        if(rankCan){
            return true;
        }else{
            EnumMap<Permission, Object> personalPlayerPermissions = data.getPermissions();
            if(!personalPlayerPermissions.isEmpty()){
                return (boolean) personalPlayerPermissions.get(permission);
            }else{
                return false;
            }
        }
    }

    public boolean isInDivision(Player player){
        return getByPlayerObj(player).getDivision() != null;
    }

    public boolean isInSameDivision(Player p1, Player p2){
        return getByPlayerObj(p1).getDivision().equalsIgnoreCase(getByPlayerObj(p2).getDivision());
    }
}
