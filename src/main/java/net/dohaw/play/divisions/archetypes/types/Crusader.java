package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Crusader extends ArchetypeWrapper {

    public Crusader(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public ItemStack[] getBeginnerItems() {
        return new ItemStack[0];
    }

    @Override
    public Map<SpecialityKey, Speciality> getSpecialities() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("crusader", "crus", "cr");
    }

}
