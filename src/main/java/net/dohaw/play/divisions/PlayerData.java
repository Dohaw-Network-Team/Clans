package net.dohaw.play.divisions;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import net.dohaw.play.divisions.runnables.Regener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    @Getter final private String PLAYER_NAME;
    @Getter final private UUID PLAYER_UUID;
    @Getter final private FileConfiguration PLAYER_CONFIG;

    @Getter @Setter private String division = null;

    @Getter @Setter private Rank rank;
    @Getter @Setter private int kills, casualties, shrinesConquered, level;
    @Getter @Setter private double heartsDestroyed, exp, regen;
    @Getter @Setter private DivisionChannel channel = DivisionChannel.NONE;
    @Getter @Setter private ItemCreationSession itemCreationSession = new ItemCreationSession();

    /*
        Archetype stuff
     */
    @Getter @Setter private ArchetypeWrapper archetype;
    @Getter @Setter private SpecialityWrapper speciality;
    @Getter @Setter private Regener regener;
    @Getter @Setter private EnumMap<Stat, Double> statLevels = new EnumMap<>(Stat.class);
    @Getter private Map<String, Long> spellCoolDowns = new HashMap<>();

    @Getter private EnumMap<Permission, Object> permissions = new EnumMap<>(Permission.class);

    public PlayerData(final OfflinePlayer PLAYER, final FileConfiguration PLAYER_CONFIG, Rank rank){
        this.PLAYER_NAME = PLAYER.getName();
        this.PLAYER_UUID = PLAYER.getUniqueId();
        this.rank = rank;
        this.PLAYER_CONFIG = PLAYER_CONFIG;
    }

    public void replacePermission(Permission perm, Object value){
        permissions.replace(perm, value);
    }

    public void putPermission(Permission perm, Object value){
        permissions.put(perm, value);
    }

    public void startRegener(DivisionsPlugin plugin, long interval){
        regener = new Regener(plugin, PLAYER_UUID);
        regener.runTaskTimer(plugin, 0L, interval);
    }

    public void stopRegener(){
        regener.cancel();
    }

    public void restartRegener(DivisionsPlugin plugin, long interval){
        stopRegener();
        startRegener(plugin, interval);
    }

    public void addCoolDown(String spellKey, double coolDownTime){
        long millis = (long) (coolDownTime * 1000);
        long millisCooldownEnd = millis + System.currentTimeMillis();
        spellCoolDowns.put(spellKey, millisCooldownEnd);
    }

    public void removeCoolDown(String spellKey){
        spellCoolDowns.remove(spellKey);
    }

    public boolean isOnCooldown(String spellKey){
        return spellCoolDowns.containsKey(spellKey);
    }

    public long getCooldownEnd(String spellKey){
        return spellCoolDowns.get(spellKey);
    }

    public boolean hasPermission(Permission perm){
        return permissions.get(perm) != null;
    }

    public OfflinePlayer getPlayer(){
        return Bukkit.getOfflinePlayer(getPLAYER_UUID());
    }

}
