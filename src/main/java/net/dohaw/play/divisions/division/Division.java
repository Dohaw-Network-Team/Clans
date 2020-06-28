package net.dohaw.play.divisions.division;

import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class Division {

    final private String NAME;
    final private FileConfiguration CONFIG;
    final private PlayerData LEADER;

    private DivisionStatus status;
    private String bankName;
    private double power, heartsDestroyed;
    private Location garrisonLocation;
    private List<PlayerData> players;
    private EnumMap<Rank, EnumMap<Permission, Object>> rankPermissions = new EnumMap<>(Rank.class);
    private int kills, casualties, shrinesConquered, numMembers;

    public Division(final String DIVISION_NAME, final FileConfiguration DIVISION_CONFIG, final PlayerData LEADER){
        this.NAME = DIVISION_NAME;
        this.CONFIG = DIVISION_CONFIG;
        this.LEADER = LEADER;
        this.players = new ArrayList<>();
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerData> players) {
        this.players = players;
    }

    public void addPlayer(PlayerData data){
        players.add(data);
    }

    public void removePlayer(PlayerData data){
        players.remove(data);
    }

    public Location getGarrisonLocation() {
        return garrisonLocation;
    }

    public void setGarrisonLocation(Location garrisonLocation) {
        this.garrisonLocation = garrisonLocation;
    }

    public int getCasualties() {
        return casualties;
    }

    public void setCasualties(int casualties) {
        this.casualties = casualties;
    }

    public double getHeartsDestroyed() {
        return heartsDestroyed;
    }

    public void setHeartsDestroyed(double heartsDestroyed) {
        this.heartsDestroyed = heartsDestroyed;
    }

    public double getGoldAmount() {
        return DivisionsPlugin.getEconomy().bankBalance(bankName).balance;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getShrinesConquered() {
        return shrinesConquered;
    }

    public void setShrinesConquered(int shrinesConquered) {
        this.shrinesConquered = shrinesConquered;
    }

    public PlayerData getMember(UUID u){
        for(PlayerData data : players){
            if(data.getPlayerUUID().equals(u)){
                return data;
            }
        }
        return null;
    }

    public String getName() {
        return NAME;
    }

    public FileConfiguration getConfig() {
        return CONFIG;
    }

    public PlayerData getLeader() {
        return LEADER;
    }

    public EnumMap<Rank, EnumMap<Permission, Object>> getRankPermissions() {
        return rankPermissions;
    }

    public void setRankPermissions(EnumMap<Rank, EnumMap<Permission, Object>> rankPermissions) {
        this.rankPermissions = rankPermissions;
    }

    public void setRankPermission(Rank rank, Permission perm, Object value){
        rankPermissions.get(rank).replace(perm, value);
    }

    public DivisionStatus getStatus() {
        return status;
    }

    public void setStatus(DivisionStatus status) {
        this.status = status;
    }
}
