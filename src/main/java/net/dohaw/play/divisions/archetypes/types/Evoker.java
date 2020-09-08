package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;

import java.util.Arrays;
import java.util.List;

public class Evoker extends ArchetypeWrapper {

    public Evoker(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("evoker", "ev", "evo");
    }

}
