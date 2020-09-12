package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.RegenType;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assassin extends ArchetypeWrapper {
    @Override
    public Map<Stat, Double> getDefaultStats() {
        return null;
    }

    public Assassin(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ass", "assassin", "assass");
    }

    @Override
    public List<String> getDefaultItems() {
        return null;
    }

    @Override
    public Map<SpecialityKey, SpecialityWrapper> getSpecialities() {
        return new HashMap<SpecialityKey, SpecialityWrapper>(){{
            put(SpecialityKey.PROACTIVE, Speciality.PROACTIVE);
            put(SpecialityKey.SHADOW, Speciality.SHADOW);
            put(SpecialityKey.VENOM, Speciality.VENOM);
        }};
    }

    @Override
    public List<Material> getProficientItems() {
        return null;
    }

    @Override
    public RegenType getRegenType() {
        return null;
    }

    @Override
    public Map<SpellKey, SpellWrapper> getSpells() {
        return null;
    }
}
