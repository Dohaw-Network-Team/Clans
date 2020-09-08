package net.dohaw.play.divisions.archetypes;

import net.dohaw.play.divisions.archetypes.types.*;

import java.util.Map;

public abstract class Archetype extends WrapperHolder{

    public static final ArchetypeWrapper ASSASSIN = new Assassin(ArchetypeKey.ASSASSIN);
    public static final ArchetypeWrapper ARCHER = new Archer(ArchetypeKey.ARCHER);
    public static final ArchetypeWrapper CRUSADER = new Assassin(ArchetypeKey.CRUSADER);
    public static final ArchetypeWrapper DUELIST = new Duelist(ArchetypeKey.DUELIST);
    public static final ArchetypeWrapper EVOKER = new Evoker(ArchetypeKey.EVOKER);
    public static final ArchetypeWrapper WIZARD = new Wizard(ArchetypeKey.WIZARD);
    public static final ArchetypeWrapper TREE = new Tree(ArchetypeKey.TREE);
    public static final ArchetypeWrapper CLERIC = new Cleric(ArchetypeKey.CLERIC);

}
