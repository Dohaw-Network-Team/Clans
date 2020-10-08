package net.dohaw.play.divisions.archetypes.spells.active.archer;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Escape extends ActiveSpell {

    public final int ABSORPTION_DURATION = 4;

    public Escape(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(PlayerData pd) {

        Player player = pd.getPlayer().getPlayer();
        Vector playerDirection = player.getLocation().getDirection();
        double scale = defaultConfig.getEscapeScale() * -1;

        playerDirection.multiply(scale);
        playerDirection.setY(playerDirection.getY() + 0.6);
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
        return Arrays.asList(
            "You get sent backwards in an attempt to escape",
            "You are given absorption (the level of it is dependant on your level) for " + ABSORPTION_DURATION + " seconds"
        );
    }

    @Override
    public double getBaseCooldown() {
        return 20;
    }

    @Override
    public boolean displayCooldownMessage() {
        return true;
    }

    @Override
    public List<String> getCooldownLorePart(PlayerData pd) {
        return Collections.singletonList(getBaseCooldown() + " seconds");
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.SPELL;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }
}
