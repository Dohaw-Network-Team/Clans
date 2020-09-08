package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.WrapperHolder;

public abstract class Spell extends WrapperHolder {

    public static final SpellWrapper INVISIBLE_STRIKE = new SpellWrapper("evoker_default_staff", ArchetypeKey.EVOKER, SpellKey.INVISIBLE_STRIKE);
    public static final SpellWrapper SMITE = new SpellWrapper("cleric_default_staff", ArchetypeKey.CLERIC, SpellKey.SMITE);
    public static final SpellWrapper FROST_STRIKE = new SpellWrapper("wizard_default_staff", ArchetypeKey.WIZARD, SpellKey.FROST_STRIKE);

}
