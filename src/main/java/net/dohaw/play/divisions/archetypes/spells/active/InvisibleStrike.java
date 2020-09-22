package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.RegenType;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class InvisibleStrike extends ActiveSpell implements Damageable, ActiveLaunchableSpell {

    public InvisibleStrike(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(Player player, boolean outOrIn) {

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
        return Arrays.asList("Hits your enemies with an attack that is obscure and undetectable");
    }

    @Override
    public double getCooldown() {
        return 0.5;
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }


    @Override
    public Material getProjectileMaterial() {
        return null;
    }
}
