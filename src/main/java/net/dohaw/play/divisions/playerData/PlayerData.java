package net.dohaw.play.divisions.playerData;

import net.dohaw.play.divisions.Division;
import net.dohaw.play.divisions.rank.Rank;
import net.dohaw.play.divisions.rank.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    final private OfflinePlayer PLAYER;
    final private String PLAYER_NAME;
    final private UUID PLAYER_UUID;
    final private FileConfiguration PLAYER_CONFIG;
    private String playerDivision = null;
    private Rank rank = null;
    private HashMap<Permission, Object> playerPermissions;
    private int kills, casualties, shrinesConquered;
    private double heartsDestroyed;

    public PlayerData(final OfflinePlayer PLAYER, final FileConfiguration PLAYER_CONFIG, Rank rank){
        this.PLAYER = PLAYER;
        this.PLAYER_NAME = PLAYER.getName();
        this.PLAYER_UUID = PLAYER.getUniqueId();
        this.rank = rank;
        this.PLAYER_CONFIG = PLAYER_CONFIG;
    }

    public String getPlayerName(){
        return PLAYER_NAME;
    }

    public HashMap<Permission, Object> getPermissions(){
        return playerPermissions;
    }

    public UUID getPlayerUUID(){
        return PLAYER_UUID;
    }

    public void setPermission(Permission perm, Object value){
        playerPermissions.replace(perm, value);
    }

    public boolean hasPermission(Permission perm){
        return playerPermissions.get(perm) != null;
    }

    public String getDivision(){
        return playerDivision;
    }

    public void savePermissions(){

    }

    public OfflinePlayer getPlayer() {
        return PLAYER;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getCasualties() {
        return casualties;
    }

    public void setCasualties(int casualties) {
        this.casualties = casualties;
    }

    public int getShrinesConquered() {
        return shrinesConquered;
    }

    public void setShrinesConquered(int shrinesConquered) {
        this.shrinesConquered = shrinesConquered;
    }

    public void setPlayerDivision(String playerDivision) {
        this.playerDivision = playerDivision;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public FileConfiguration getPlayerConfig() {
        return PLAYER_CONFIG;
    }

    public double getHeartsDestroyed() {
        return heartsDestroyed;
    }

    public void setHeartsDestroyed(double heartsDestroyed) {
        this.heartsDestroyed = heartsDestroyed;
    }
}
