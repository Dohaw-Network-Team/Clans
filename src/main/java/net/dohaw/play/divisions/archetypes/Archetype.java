package net.dohaw.play.divisions.archetypes;

import net.dohaw.play.divisions.archetypes.types.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Archetype {

    public static final ArchetypeWrapper ASSASSIN = new Assassin(ArchetypeName.ASSASSIN);
    public static final ArchetypeWrapper ARCHER = new Archer(ArchetypeName.ARCHER);
    public static final ArchetypeWrapper CRUSADER = new Assassin(ArchetypeName.CRUSADER);
    public static final ArchetypeWrapper DUELIST = new Duelist(ArchetypeName.DUELIST);
    public static final ArchetypeWrapper EVOKER = new Evoker(ArchetypeName.EVOKER);
    public static final ArchetypeWrapper WIZARD = new Wizard(ArchetypeName.WIZARD);

    private static Map<ArchetypeName, ArchetypeWrapper> archetypes = new HashMap<>();

    public static ArchetypeWrapper getByKey(ArchetypeName archetypeName){
        return archetypes.get(archetypeName);
    }

    public static ArchetypeWrapper getByAlias(String alias){
        for(Map.Entry<ArchetypeName, ArchetypeWrapper> entry : archetypes.entrySet()){
            ArchetypeWrapper archetype = entry.getValue();
            if(archetype.getAliases().contains(alias)){
                return archetype;
            }
        }
        return null;
    }

    public static void registerArchetype(ArchetypeWrapper archetype){
        if(archetypes.containsKey(archetype.getARCHETYPE())){
            throw new IllegalArgumentException("This archetype is already registered!");
        }
        archetypes.put(archetype.getARCHETYPE(), archetype);
    }
}
