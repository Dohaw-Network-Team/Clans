package net.dohaw.play.divisions.archetypes.specializations;

import lombok.Getter;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;

public enum SpecialityKey {

    DIRECT("Direct", ArchetypeKey.CLERIC),
    SPREAD("Spread", ArchetypeKey.CLERIC),
    VAMPIRIC("Vampiric", ArchetypeKey.CLERIC),

    TEMPEST("Tempest", ArchetypeKey.WIZARD),
    ICE("Ice", ArchetypeKey.WIZARD),
    FIRE("Fire", ArchetypeKey.WIZARD),

    SOUL_PIERCING("Soul Piercing", ArchetypeKey.ARCHER),
    CLOAK("Cloak", ArchetypeKey.ARCHER),
    DECEPTION("Deception", ArchetypeKey.ARCHER),
    CONTROL("Control", ArchetypeKey.ARCHER),

    ELEMENTAL("Elemental", ArchetypeKey.EVOKER),
    DESTRUCTION("Destruction", ArchetypeKey.EVOKER),
    CONSCIOUS("Conscious", ArchetypeKey.EVOKER),

    ORDER("Order", ArchetypeKey.CRUSADER),
    PROTECTION("Protection", ArchetypeKey.CRUSADER),

    VENOM("Venom", ArchetypeKey.ASSASSIN),
    PROACTIVE("Pro-active", ArchetypeKey.ASSASSIN),
    SHADOW("Shadow", ArchetypeKey.ASSASSIN),

    UNIFORM("Uniform", ArchetypeKey.DUELIST),
    SOUL("Soul", ArchetypeKey.DUELIST),
    PSYCHOTIC("Psychotic", ArchetypeKey.DUELIST);

    @Getter private ArchetypeKey archetype;
    @Getter private String key;
    SpecialityKey(String key, ArchetypeKey archetype){
        this.archetype = archetype;
        this.key = key;
    }

}
