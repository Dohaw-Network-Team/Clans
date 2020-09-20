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

    /*
        isOut == whether we are executing on the player that is doing the spell or the player that is being affected by the spell
     */
    public abstract void execute(Player player, boolean isOut);

    public abstract RegenType getRegenTypeAffected();

    public abstract double getPercentageRegenAffected();

    public abstract double getCooldown();

    public abstract boolean displayCooldownMessage();

    public abstract double alterDamage();

}
