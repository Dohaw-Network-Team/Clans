package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.RegenType;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import org.bukkit.entity.Player;

public abstract class ActiveSpell extends SpellWrapper implements Cooldownable {

    public ActiveSpell(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    /*
    isOut == whether we are executing on the player that is doing the spell or the player that is being affected by the spell
 */
    public abstract void execute(Player player, boolean isOut);

    public abstract double getPercentageRegenAffected();

}
