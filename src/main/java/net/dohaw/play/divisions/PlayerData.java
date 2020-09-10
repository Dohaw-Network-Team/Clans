package net.dohaw.play.divisions;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.UUID;

public class PlayerData {

    @Getter final private OfflinePlayer PLAYER;
    @Getter final private String PLAYER_NAME;
    @Getter final private UUID PLAYER_UUID;
    @Getter final private FileConfiguration PLAYER_CONFIG;

    @Getter @Setter private String division = null;

    @Getter @Setter private Rank rank;
    @Getter @Setter private int kills, casualties, shrinesConquered, level;
    @Getter @Setter private double heartsDestroyed, exp, mana;
    @Getter @Setter private DivisionChannel channel = DivisionChannel.NONE;
    @Getter @Setter private ItemCreationSession itemCreationSession = new ItemCreationSession();

    /*
        Archetype stuff
     */
    @Getter @Setter private ArchetypeWrapper archetype;
    @Getter @Setter private SpecialityWrapper speciality;
    @Getter @Setter private EnumMap<Stat, Double> statLevels = new EnumMap<>(Stat.class);

    @Getter private EnumMap<Permission, Object> permissions = new EnumMap<>(Permission.class);

    public PlayerData(final OfflinePlayer PLAYER, final FileConfiguration PLAYER_CONFIG, Rank rank){
        this.PLAYER = PLAYER;
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

    public boolean hasPermission(Permission perm){
        return permissions.get(perm) != null;
    }

}
