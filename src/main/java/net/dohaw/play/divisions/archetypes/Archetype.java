package net.dohaw.play.divisions.archetypes;

import lombok.Getter;
import lombok.Setter;

public abstract class Archetype implements Specializer{

    @Getter @Setter private SpecialityName speciality;
    @Getter private final ArchetypeName ARCHETYPE;
    @Getter private boolean canDuelWield;

    public Archetype(final ArchetypeName ARCHETYPE){
        this.ARCHETYPE = ARCHETYPE;
    }

}
