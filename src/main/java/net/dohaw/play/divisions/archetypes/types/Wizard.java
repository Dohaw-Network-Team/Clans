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
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Wizard extends ArchetypeWrapper {

    public Wizard(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public Map<Stat, Double> getDefaultStats() {
        return null;
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        return new ArrayList<>();
    }

    @Override
    public List<ItemStack> getProficientItems() {
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
    public Map<SpecialityKey, SpecialityWrapper> getSpecialities() {
        return new HashMap<SpecialityKey, SpecialityWrapper>(){{
            put(SpecialityKey.FIRE, Speciality.FIRE);
            put(SpecialityKey.ICE, Speciality.ICE);
            put(SpecialityKey.TEMPEST, Speciality.TEMPEST);
        }};
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("wizard", "wiz", "wzrd");
    }

}
