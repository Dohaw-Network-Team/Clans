package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftSnowball;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.List;

public class FrostStrike extends ActiveLaunchableSpell implements Damageable {

    public FrostStrike(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public Material getProjectileMaterial() {
        return Material.PACKED_ICE;
    }

    @Override
    public List<String> getCooldownLorePart() {
        return Arrays.asList(getCooldown() + " seconds");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Shoots a frost strike");
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
        return 0;
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("Damage is based on spell power");
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return null;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }

    @Override
    public double getCooldown() {
        return 0.5;
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }

}
