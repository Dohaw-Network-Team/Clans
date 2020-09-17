package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.RegenType;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;

public class Tree extends ArchetypeWrapper {
    /*
        For HyperTree101
     */
    public Tree(ArchetypeKey KEY) {
        super(KEY);
    }

    @Override
    public List<Object> getDefaultItems() {
        return null;
    }

    @Override
    public EnumMap<Stat, Double> getDefaultStats() {
        return null;
    }

    @Override
    public EnumMap<SpecialityKey, SpecialityWrapper> getSpecialities() {
        return null;
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
    public EnumMap<SpellKey, SpellWrapper> getSpells() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
