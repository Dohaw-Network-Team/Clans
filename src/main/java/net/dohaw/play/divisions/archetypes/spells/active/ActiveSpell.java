package net.dohaw.play.divisions.archetypes.spells.active;

import lombok.Getter;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.RegenAffectable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import org.bukkit.entity.Player;

public abstract class ActiveSpell extends SpellWrapper implements Cooldownable, RegenAffectable {

    @Getter final String LORE_COLOR = "&e";

    /*
        An active spell is defined as a spell that is activated via custom item
     */
    public ActiveSpell(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    /*
        isOut == whether we are executing on the player that is doing the spell or the player that is being affected by the spell
    */
    public abstract void execute(Player player);

}
