package net.dohaw.play.divisions.archetypes.spells;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.RegenType;
import net.dohaw.play.divisions.archetypes.Wrapper;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellWrapper extends Wrapper {

    @Getter @Setter private ArchetypeKey archetype;
    @Getter @Setter private String customItemBindedToKey;
    @Getter private int levelUnlocked;

    public SpellWrapper(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY, int levelUnlocked) {
        super(KEY);
        this.customItemBindedToKey = customItemBindedToKey;
        this.archetype = archetype;
        this.levelUnlocked = levelUnlocked;
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add(getName());
        }};
    }

    public abstract void execute(Player player);

    public abstract RegenType getRegenTypeAffected();

    public abstract double getPercentageRegenAffected();

    public abstract double getCooldown();

}
