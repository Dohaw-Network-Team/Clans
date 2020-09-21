package net.dohaw.play.divisions.archetypes.spells.passive;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;

public abstract class PassiveSpell extends SpellWrapper{

    public PassiveSpell(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

}
