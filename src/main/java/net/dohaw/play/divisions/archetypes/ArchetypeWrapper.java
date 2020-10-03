package net.dohaw.play.divisions.archetypes;

import lombok.Getter;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;

public abstract class ArchetypeWrapper extends Wrapper{

    @Getter final protected ArchetypeKey KEY;

    public ArchetypeWrapper(ArchetypeKey KEY) {
        super(KEY);
        this.KEY = KEY;
    }

    /*
        Default item string keys. Gets the CustomItem object via the CustomItemManager class
     */
    public abstract List<Object> getDefaultItems();

    public abstract EnumMap<Stat, Double> getDefaultStats();

    public abstract EnumMap<SpecialityKey, SpecialityWrapper> getSpecialities();

    public abstract List<Material> getProficientItems();

    public abstract RegenType getRegenType();

    public abstract ChatColor getColor();

}
