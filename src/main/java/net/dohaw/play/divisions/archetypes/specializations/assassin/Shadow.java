package net.dohaw.play.divisions.archetypes.specializations.assassin;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;

import java.util.List;
import java.util.Map;

public class Shadow extends SpecialityWrapper {
    @Override
    public List<String> getAliases() {
        return null;
    }

    public Shadow(SpecialityKey KEY) {
        super(KEY);
    }

    @Override
    public Map<Stat, Double> getMaxStats() {
        return null;
    }
}
