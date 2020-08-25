package net.dohaw.play.divisions.managers;

import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.files.PlayerDataHandler;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
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
            if(data.getPlayerUUID().equals(playerUUID)){
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

    @Override
    public boolean addContent(Object content) { return false; }

    public void addPlayerData(UUID u){
        playerDataList.add(playerDataHandler.loadPlayerData(u));
    }

    public void setPlayerData(UUID playerUUID, PlayerData newData){
        PlayerData playerData = getPlayerByUUID(playerUUID);
        playerDataList.set(playerDataList.indexOf(playerData), newData);
    }

    public void removePlayerData(UUID u){
        playerDataHandler.saveData(getPlayerByUUID(u));
        playerDataList.remove(getPlayerByUUID(u));
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
        FileConfiguration playerDataConfig = data.getPlayerConfig();
        if(!playerDataConfig.getString("Division").equalsIgnoreCase("none")){
            data.setPlayerDivision((playerDataConfig.getString("Division")));
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
        EnumMap<Permission, Object> kickerRankPermissions = divisionRankPermissions.get(rank);
        boolean rankCan = (boolean) kickerRankPermissions.get(permission);

        if(rankCan){
            return true;
        }else{
            EnumMap<Permission, Object> personalPlayerPermissions = data.getPermissions();
            if(!personalPlayerPermissions.isEmpty()){
                return (boolean) personalPlayerPermissions.get(Permission.CAN_KICK_PLAYERS);
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
