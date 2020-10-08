package net.dohaw.play.divisions.archetypes.spells.duelist;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveHittableSpell;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Stun extends ActiveSpell implements ActiveHittableSpell {

    final double KNOCK_UP_DISTANCE = 2;

    public Stun(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(PlayerData pd) { }

    /*
        Fix this off stream
     */
    @Override
    public void executeHit(Entity hitEntity, Player hitter) {
        Location entityDirection = hitEntity.getLocation().add(0, KNOCK_UP_DISTANCE, 0);
        hitEntity.teleport(entityDirection);
    }

    @Override
    public double getBaseCooldown() {
        return 30;
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
        return .10;
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
        return Arrays.asList("Knocks up opponent in front of you");
    }

}
