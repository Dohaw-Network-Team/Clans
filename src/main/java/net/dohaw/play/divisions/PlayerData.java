package net.dohaw.play.divisions;

import net.dohaw.play.divisions.rank.RankPermission;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private Player player;
    private String playerName;
    private UUID playerUUID;
    private Division playerDivision;
    private HashMap<RankPermission, Boolean> playerPermissions;

    public PlayerData(Player p){
        this.player = p;
        this.playerName = p.getName();
        this.playerUUID = p.getUniqueId();
    }

    public String getPlayerName(){
        return playerName;
    }

    public HashMap<RankPermission, Boolean> getPermissions(){
        return playerPermissions;
    }

    public UUID getPlayerUUID(){
        return playerUUID;
    }

    public void setPermission(RankPermission perm, boolean value){
        playerPermissions.replace(perm, value);
    }

    public boolean hasPermission(RankPermission perm){
        return playerPermissions.get(perm);
    }

    public Division getDivision(){
        return playerDivision;
    }

    public void savePermissions(){

    }

}
