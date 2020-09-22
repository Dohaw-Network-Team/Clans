package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Escape extends ActiveSpell {

    public final int ABSORPTION_DURATION = 4;

    public Escape(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(Player player) {

        Vector playerDirection = player.getLocation().getDirection();
        double scale = defaultConfig.getEscapeScale();

        playerDirection.multiply(scale);
        playerDirection.setY(playerDirection.getY() + 0.6);
        player.setVelocity(playerDirection);

        player.setVelocity(playerDirection);

        World world = player.getWorld();
        world.spawnParticle(getSpellOwnerParticle(), player.getLocation(), 40);

    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.15;
    }

    @Override
    public List<String> getDescription() {
        int regenAmount = (int) (getPercentageRegenAffected() * 100);
        return Arrays.asList(
            "You get sent backwards in an attempt to escape",
            "You are given absorption (the level of it is dependant on your level) for " + ABSORPTION_DURATION + " seconds",
            "Uses " + regenAmount + " regen"
        );
    }

    @Override
    public double getCooldown() {
        return 20;
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
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
    public double getRange() {
        return 0;
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.END_ROD;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }
}
