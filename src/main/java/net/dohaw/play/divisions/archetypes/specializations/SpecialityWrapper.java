package net.dohaw.play.divisions.archetypes.specializations;


import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.Wrapper;

import java.util.Map;

public abstract class SpecialityWrapper extends Wrapper {

    public SpecialityWrapper(final SpecialityKey KEY){
        super(KEY);
    }

    public abstract Map<Stat, Double> getMaxStats();

    public abstract boolean canDuelWield();

}
