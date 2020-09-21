package net.dohaw.play.divisions.archetypes.spells;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.Wrapper;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellWrapper extends Wrapper {

    protected static DivisionsPlugin plugin;
    protected DefaultConfig defaultConfig;

    @Getter @Setter private ArchetypeKey archetype;
    @Getter @Setter private String customItemBindedToKey;
    @Getter private int levelUnlocked;

    public SpellWrapper(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY, int levelUnlocked) {
        super(KEY);
        this.customItemBindedToKey = customItemBindedToKey;
        this.archetype = archetype;
        this.levelUnlocked = levelUnlocked;

        plugin = DivisionsPlugin.getInstance();
        this.defaultConfig = plugin.getDefaultConfig();
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add(getName());
        }};
    }

    public abstract double getCooldown();

    public abstract boolean displayCooldownMessage();

    public abstract double alterDamage(double dmg, PlayerData pd);

    public abstract double getRange();

    public abstract Particle getSpellOwnerParticle();

    public abstract Particle getSpellAffecterParticle();

}
