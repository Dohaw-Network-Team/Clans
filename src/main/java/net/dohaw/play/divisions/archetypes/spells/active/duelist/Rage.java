package net.dohaw.play.divisions.archetypes.spells.active.duelist;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Affectable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Rage extends ActiveSpell implements Affectable {

    final double STRENGTH_INCREASE = .20;

    @Override
    public List<String> getDurationLorePart() {
        return Arrays.asList(getDuration() + " seconds");
    }

    public Rage(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(PlayerData pd) {

        Player player = pd.getPlayer().getPlayer();
        Map<Stat, Double> playerStats = pd.getStatLevels();
        final double INIT_STRENGTH = playerStats.get(Stat.STRENGTH);
        double additive = INIT_STRENGTH * STRENGTH_INCREASE;
        double finalStrength = INIT_STRENGTH + additive;

        pd.setStat(Stat.STRENGTH, finalStrength);

        plugin.getPlayerDataManager().updatePlayerData(pd);

        World world = player.getWorld();
        world.spawnParticle(getSpellOwnerParticle(), player.getLocation(), 40, 2, 2, 2);

        /*
            Thing that sets the player's strength back to normal
         */
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            pd.setStat(Stat.STRENGTH, INIT_STRENGTH);
            plugin.getPlayerDataManager().updatePlayerData(pd);
        }, (long) (getDuration() * 20) );

    }

    @Override
    public double getBaseCooldown() {
        return 60;
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public List<String> getCooldownLorePart(PlayerData pd) {
        return Arrays.asList(getBaseCooldown() + " seconds");
    }

    @Override
    public double getPercentageRegenAffected() {
        return .20;
    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
        return 0;
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.FLAME;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Increases your strength for " + (STRENGTH_INCREASE * 100) + "%");
    }

    @Override
    public double getDuration() {
        return 5;
    }
}
