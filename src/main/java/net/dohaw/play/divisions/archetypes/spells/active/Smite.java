package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Smite extends ActiveSpell implements Damageable {

    public Smite(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }

    @Override
    public double getCooldown() {
        return 0.5;
    }

    @Override
    public void execute(Player player, boolean outOrIn) {

    }

    @Override
    public double getRange() {
        return 0;
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
    public List<String> getCooldownLorePart() {
        return Arrays.asList("");
    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
        return 0;
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("");
    }
}
