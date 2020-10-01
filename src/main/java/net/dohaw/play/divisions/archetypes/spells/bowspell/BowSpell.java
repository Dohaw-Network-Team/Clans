package net.dohaw.play.divisions.archetypes.spells.bowspell;

import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;

public abstract class BowSpell extends PassiveSpell {

    public BowSpell(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }


}
