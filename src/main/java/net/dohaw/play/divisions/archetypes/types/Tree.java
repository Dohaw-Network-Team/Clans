package net.dohaw.play.divisions.archetypes.types;

import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.RegenType;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityKey;
import net.dohaw.play.divisions.archetypes.specializations.SpecialityWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
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
    public ChatColor getColor() {
        return ChatColor.DARK_GREEN;
    }

    @Override
    public List<Object> getDefaultItems() {
        return Arrays.asList(
                Material.LEATHER_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS
        );
    }

    @Override
    public EnumMap<Stat, Double> getDefaultStats() {
        return new EnumMap<Stat, Double>(Stat.class){{
            put(Stat.STRENGTH, 1.0);
            put(Stat.SPELL_POWER, 1.0);
            put(Stat.FORTITUDE, 1.0);
            put(Stat.MITIGATION, 1.0);
            put(Stat.QUICKNESS, 1.0);
            put(Stat.STEALTHINESS, 1.0);
            put(Stat.ACCURACY, 1.0);
            put(Stat.LUCK, 2.0);
            put(Stat.PIERCING, 1.0);
            put(Stat.MAX_HEALTH, 1.0);
            put(Stat.RESTORATION, 1.0);
        }};
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
    public List<String> getAliases() {
        return Arrays.asList("tree", "tre", "tr");
    }
}
