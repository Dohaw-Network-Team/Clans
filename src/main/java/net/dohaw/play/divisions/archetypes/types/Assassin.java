package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeName;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;

import java.util.Arrays;
import java.util.List;

public class Assassin extends ArchetypeWrapper {

    public Assassin(ArchetypeName ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ass", "assassin", "assass");
    }

}
