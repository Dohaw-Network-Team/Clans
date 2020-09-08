package net.dohaw.play.divisions.archetypes.specializations;


import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.Wrapper;

import java.util.List;
import java.util.Map;

public abstract class SpecialityWrapper extends Wrapper {

    public SpecialityWrapper(final SpecialityKey KEY){
        super(KEY);
    }

    public String getName(){
        String archetypeStr = KEY.name();
        return archetypeStr.substring(0, 1).toUpperCase() + archetypeStr.substring(1).toLowerCase();
    }

    public abstract List<String> getAliases();

    public abstract Map<Stat, Double> getMaxStats();

}
