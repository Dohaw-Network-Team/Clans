package net.dohaw.play.divisions.archetypes.specializations;


import lombok.Getter;

import java.util.List;

public abstract class SpecialityWrapper {

    @Getter protected final SpecialityKey KEY;

    public SpecialityWrapper(final SpecialityKey KEY){
        this.KEY = KEY;
    }

    public String getName(){
        String archetypeStr = KEY.name();
        return archetypeStr.substring(0, 1).toUpperCase() + archetypeStr.substring(1).toLowerCase();
    }

    public abstract List<String> getAliases();

}
