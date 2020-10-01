package net.dohaw.play.divisions.archetypes.spells;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.Wrapper;
import net.dohaw.play.divisions.files.DefaultConfig;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellWrapper extends Wrapper {

    protected static DivisionsPlugin plugin;
    protected DefaultConfig defaultConfig;

    @Getter @Setter protected ArchetypeWrapper archetype;
    @Getter @Setter protected String customItemBindedToKey;
    @Getter protected int levelUnlocked;

    public SpellWrapper(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
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

    public abstract double alterDamage(double dmg, PlayerData pd);

    public abstract Particle getSpellOwnerParticle();

    public abstract Particle getSpellAffecterParticle();

    public abstract List<String> getDescription();

    public abstract boolean isBowSpell();

}
