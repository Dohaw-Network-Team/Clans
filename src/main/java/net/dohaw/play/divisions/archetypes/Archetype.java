package net.dohaw.play.divisions.archetypes;

import net.dohaw.play.divisions.archetypes.types.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Archetype {

    public static final ArchetypeWrapper ASSASSIN = new Assassin(ArchetypeKey.ASSASSIN);
    public static final ArchetypeWrapper ARCHER = new Archer(ArchetypeKey.ARCHER);
    public static final ArchetypeWrapper CRUSADER = new Assassin(ArchetypeKey.CRUSADER);
    public static final ArchetypeWrapper DUELIST = new Duelist(ArchetypeKey.DUELIST);
    public static final ArchetypeWrapper EVOKER = new Evoker(ArchetypeKey.EVOKER);
    public static final ArchetypeWrapper WIZARD = new Wizard(ArchetypeKey.WIZARD);
    public static final ArchetypeWrapper CLERIC = new Cleric(ArchetypeKey.CLERIC);

    public static Map<ArchetypeKey, ArchetypeWrapper> archetypes = new HashMap<>();

    public static ArchetypeWrapper getByKey(ArchetypeKey archetypeKey){
        return archetypes.get(archetypeKey);
    }

    public static ArchetypeWrapper getByAlias(String alias){
        for(Map.Entry<ArchetypeKey, ArchetypeWrapper> entry : archetypes.entrySet()){
            ArchetypeWrapper archetype = entry.getValue();
            if(archetype.getAliases().contains(alias)){
                return archetype;
            }
        }
        return null;
    }

    public static void registerArchetype(ArchetypeWrapper archetype){
        if(archetypes.containsKey(archetype.getKEY())){
            throw new IllegalArgumentException("This archetype is already registered!");
        }
        archetypes.put(archetype.getKEY(), archetype);
    }
}
