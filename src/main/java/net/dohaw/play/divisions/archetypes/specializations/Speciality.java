package net.dohaw.play.divisions.archetypes.specializations;

import net.dohaw.play.divisions.Stat;

import java.util.Map;

public abstract class Speciality {

    public abstract Map<Stat, Double> getMaxStats();
    public abstract SpecialityKey getKey();

}
