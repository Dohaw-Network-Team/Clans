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

import java.util.*;

public class Assassin extends ArchetypeWrapper {
    @Override
    public EnumMap<Stat, Double> getDefaultStats() {
        return new EnumMap<Stat, Double>(Stat.class){{
            put(Stat.STRENGTH, 1.0);
            put(Stat.SPELL_POWER, 1.0);
            put(Stat.FORTITUDE, 1.0);
            put(Stat.MITIGATION, 1.0);
            put(Stat.QUICKNESS, 2.0);
            put(Stat.STEALTHINESS, 2.0);
            put(Stat.ACCURACY, 1.0);
            put(Stat.LUCK, 1.0);
            put(Stat.PIERCING, 1.0);
            put(Stat.MAX_HEALTH, 1.0);
        }};
    }

    public Assassin(ArchetypeKey ARCHETYPE) {
        super(ARCHETYPE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ass", "assassin", "assass");
    }

    @Override
    public List<Object> getDefaultItems() {
        return Arrays.asList(
                Material.LEATHER_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS,
                "default_dagger"
        );
    }

    @Override
    public EnumMap<SpecialityKey, SpecialityWrapper> getSpecialities() {
        return new EnumMap<SpecialityKey, SpecialityWrapper>(SpecialityKey.class){{
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

}
