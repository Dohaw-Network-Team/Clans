package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;

import java.util.Arrays;
import java.util.List;

public class Crusader extends ArchetypeWrapper {

    public Crusader(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("crusader", "crus", "cr");
    }

}
