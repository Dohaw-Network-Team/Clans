package net.dohaw.play.divisions.archetypes.spells.active;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.CooldownDecreasable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sporadic extends ActiveSpell implements CooldownDecreasable {

    public Sporadic(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(Player player) {

        final int DURATION = 5;
        // Certain percentage chance they will fire an arrow
        final double FIRE_CHANCE = 0.10;

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            double rand = Math.random();

            if(rand <= FIRE_CHANCE){
                player.launchProjectile(Arrow.class);
            }

        }, 0L, 1);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            task.cancel();
        }, DURATION * 20);

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
        return Arrays.asList(getAdjustedCooldown(pd) + " seconds");
    }

    @Override
    public double getPercentageRegenAffected() {
        return .35;
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
        return Arrays.asList("Allows you to continuously spew out arrows, each one firing on a random interval");
    }

    @Override
    public double getAdjustedCooldown(PlayerData pd) {

        final double DECREASE_PER_LUCK = 1.5;
        double playerTotalLuck = Calculator.getTotalStat(pd, Stat.LUCK);
        double cooldownDecrease = Math.floor(playerTotalLuck * DECREASE_PER_LUCK);
        double baseCoolDown = getBaseCooldown();

        if(playerTotalLuck != 1){
            return baseCoolDown - cooldownDecrease;
        }else{
            return baseCoolDown;
        }

    }

}
