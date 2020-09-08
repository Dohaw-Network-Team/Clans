package net.dohaw.play.divisions.archetypes;

import lombok.Getter;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public abstract class ArchetypeWrapper {

    @Getter protected final ArchetypeKey KEY;

    public ArchetypeWrapper(ArchetypeKey ARCHETYPE) {
        this.KEY = ARCHETYPE;
    }

    public String getName(){
        String archetypeStr = KEY.name();
        return archetypeStr.substring(0, 1).toUpperCase() + archetypeStr.substring(1).toLowerCase();
    }

    public abstract List<String> getAliases();

    public abstract ItemStack[] getBeginnerItems();

    public abstract Map<SpecialityKey, Speciality> getSpecialities();

}
