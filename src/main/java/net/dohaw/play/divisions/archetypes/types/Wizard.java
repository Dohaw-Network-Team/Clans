package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeName;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;

import java.util.Arrays;
import java.util.List;

public class Wizard extends ArchetypeWrapper {

    public Wizard(ArchetypeName ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("wizard", "wiz", "wzrd");
    }

}
