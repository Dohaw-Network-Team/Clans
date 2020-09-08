package net.dohaw.play.divisions.archetypes;

import lombok.Getter;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public abstract class ArchetypeWrapper extends Wrapper{

    public ArchetypeWrapper(ArchetypeKey KEY) {
        super(KEY);
    }

    public abstract List<ItemStack> getBeginnerItems();

    public abstract Map<SpecialityKey, Speciality> getSpecialities();

    public abstract List<ItemStack> getProficientItems();

}
