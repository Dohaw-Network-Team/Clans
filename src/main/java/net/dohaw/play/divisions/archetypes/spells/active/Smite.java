package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.Arrays;
import java.util.List;

public class Smite extends ActiveLaunchableSpell implements Damageable {

    public Smite(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public Material getProjectileMaterial() {
        return null;
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }

    @Override
    public double getBaseCooldown() {
        return 0.5;
    }

    @Override
    public void execute(PlayerData pd) {

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
    public List<String> getDescription() {
        return Arrays.asList("");
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public List<String> getCooldownLorePart(PlayerData pd) {
        return Arrays.asList("");
    }

    @Override
    public double alterDamage(double dmg, PlayerData spellOwner, PlayerData playerAffected) {
        return 0;
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("");
    }
}
