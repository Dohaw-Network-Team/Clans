package net.dohaw.play.divisions.archetypes.specializations.evoker;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;

import java.util.List;
import java.util.Map;

public class Destruction extends SpecialityWrapper {

    public Destruction(SpecialityKey KEY) {
        super(KEY);
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public Map<Stat, Double> getMaxStats() {
        return null;
    }

    @Override
    public boolean canDuelWield() {
        return false;
    }
}
