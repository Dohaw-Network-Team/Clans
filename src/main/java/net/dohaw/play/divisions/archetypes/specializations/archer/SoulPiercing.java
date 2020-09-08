package net.dohaw.play.divisions.archetypes.specializations.archer;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoulPiercing extends SpecialityWrapper {

    public SoulPiercing(SpecialityKey KEY) {
        super(KEY);
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public Map<Stat, Double> getMaxStats() {
        return new HashMap<Stat, Double>(){{
        }};
    }

    @Override
    public boolean canDuelWield() {
        return false;
    }

}
