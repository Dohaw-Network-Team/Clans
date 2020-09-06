package net.dohaw.play.divisions.archetypes;

import lombok.Getter;

public enum SpecialityName {

    DIRECT("Direct", ArchetypeName.CLERIC),
    SPREAD("Spread", ArchetypeName.CLERIC),
    VAMPIRIC("Vampiric", ArchetypeName.CLERIC),

    TEMPEST("Tempest", ArchetypeName.WIZARD),
    ICE("Ice", ArchetypeName.WIZARD),
    FIRE("Fire", ArchetypeName.WIZARD),

    SOUL_PIERCING("Soul Piercing", ArchetypeName.ARCHER),
    CLOAK("Cloak", ArchetypeName.ARCHER),
    DECEPTION("Deception", ArchetypeName.ARCHER),
    CONTROL("Control", ArchetypeName.ARCHER),

    ELEMENTAL("Elemental", ArchetypeName.EVOKER),
    DESTRUCTION("Destruction", ArchetypeName.EVOKER),
    CONSCIOUS("Conscious", ArchetypeName.EVOKER),

    ORDER("Order", ArchetypeName.CRUSADER),
    PROTECTION("Protection", ArchetypeName.CRUSADER),

    VENOM("Venom", ArchetypeName.ASSASSIN),
    PROACTIVE("Pro-active", ArchetypeName.ASSASSIN),
    SHADOW("Shadow", ArchetypeName.ASSASSIN),

    UNIFORM("Uniform", ArchetypeName.DUELIST),
    SOUL("Soul", ArchetypeName.DUELIST),
    PSYCHOTIC("Psychotic", ArchetypeName.DUELIST);

    @Getter private ArchetypeName archetype;
    @Getter private String key;
    SpecialityName(String key, ArchetypeName archetype){
        this.archetype = archetype;
        this.key = key;
    }

}
