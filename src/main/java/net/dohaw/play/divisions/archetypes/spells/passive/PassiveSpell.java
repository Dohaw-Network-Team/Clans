package net.dohaw.play.divisions.archetypes.spells.passive;

import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;

public abstract class PassiveSpell extends SpellWrapper{

    /*
        A passive spell is defined as a spell that activates via listener.
     */
    public PassiveSpell(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super("", archetype, KEY, levelUnlocked);
    }

}
