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

import java.util.List;
import java.util.Map;

public class Tree extends ArchetypeWrapper {
    /*
        For HyperTree101
     */
    public Tree(ArchetypeKey KEY) {
        super(KEY);
    }

    @Override
    public List<String> getDefaultItems() {
        return null;
    }

    @Override
    public Map<Stat, Double> getDefaultStats() {
        return null;
    }

    @Override
    public Map<SpecialityKey, SpecialityWrapper> getSpecialities() {
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
    public Map<SpellKey, SpellWrapper> getSpells() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return null;
    }
}
