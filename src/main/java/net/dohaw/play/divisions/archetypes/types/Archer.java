package net.dohaw.play.divisions.archetypes.types;

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

public class Archer extends ArchetypeWrapper {

    public Archer(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("archer", "arch", "ar");
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        return null;
    }

    @Override
    public Map<SpecialityKey, SpecialityWrapper> getSpecialities() {
        return new HashMap<SpecialityKey, SpecialityWrapper>(){{
            put(SpecialityKey.CLOAK, Speciality.CLOAK);
            put(SpecialityKey.CONTROL, Speciality.CONTROL);
            put(SpecialityKey.DECEPTION, Speciality.DECEPTION);
            put(SpecialityKey.SOUL_PIERCING, Speciality.SOUL_PIERCING);
        }};
    }

    @Override
    public List<ItemStack> getProficientItems() {
        return null;
    }


}
