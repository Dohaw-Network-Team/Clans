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

    @Getter protected final SpellKey KEY;

    public SpellWrapper(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(KEY);
        this.KEY = KEY;
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

    @Override
    public String toString(){
        return "Spell: " + getName() + "; Level Unlocked: " + levelUnlocked + "; Custom Item Binded To: " + customItemBindedToKey;
    }

    public abstract Particle getSpellOwnerParticle();

    public abstract Particle getSpellAffecterParticle();

    public abstract List<String> getDescription();

}
