package net.dohaw.play.divisions.archetypes;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public abstract class ArchetypeWrapper implements Specializable{

    @Getter protected final ArchetypeName ARCHETYPE;

    public ArchetypeWrapper(ArchetypeName ARCHETYPE) {
        this.ARCHETYPE = ARCHETYPE;
    }

    public abstract List<String> getAliases();

}
