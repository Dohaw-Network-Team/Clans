package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CripplingShot extends ActiveSpell implements Damageable {

    public CripplingShot(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public List<String> getDamageLorePart() {
        return null;
    }

    @Override
    public void execute(Player player) {

    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }

    @Override
    public double getCooldown() {
        return 20;
    }

    @Override
    public boolean displayCooldownMessage() {
        return true;
    }

    @Override
    public List<String> getCooldownLorePart() {
        return Arrays.asList(getCooldown() + " seconds");
    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
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
        return null;
    }

    @Override
    public boolean isBowSpell() {
        return true;
    }
}
