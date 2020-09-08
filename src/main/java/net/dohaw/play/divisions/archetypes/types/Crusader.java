package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crusader extends ArchetypeWrapper {

    public Crusader(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<ItemStack> getProficientItems() {
        return null;
    }

    @Override
    public Map<Stat, Double> getDefaultStats() {
        return null;
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        return null;
    }

    @Override
    public Map<SpecialityKey, SpecialityWrapper> getSpecialities() {
        return new HashMap<SpecialityKey, SpecialityWrapper>(){{
            put(SpecialityKey.ORDER, Speciality.ORDER);
            put(SpecialityKey.PROTECTION, Speciality.PROTECTION);
        }};
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("crusader", "crus", "cr");
    }

}
