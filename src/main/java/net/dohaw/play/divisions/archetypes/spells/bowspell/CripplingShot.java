package net.dohaw.play.divisions.archetypes.spells.bowspell;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.*;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CripplingShot extends BowSpell implements Damageable, RegenAffectable, Cooldownable, Affectable {

    public CripplingShot(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(archetype, KEY, levelUnlocked);
    }

    @Override
    public List<String> getDamageLorePart() {
        return null;
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
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
        return Arrays.asList(getBaseCooldown() + " seconds");
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
    public void affectHitPlayer(PlayerData damageTaker, PlayerData damagerDealer) {

        Player damagedPlayer = Bukkit.getPlayer(damageTaker.getPLAYER_UUID());
        final float INIT_WALKING_SPEED = damagedPlayer.getWalkSpeed();
        final double PERCENTAGE_SPEED_DECREASE = .25;

        double subtractive = INIT_WALKING_SPEED * PERCENTAGE_SPEED_DECREASE;
        float newSpeed = (float) (INIT_WALKING_SPEED - subtractive);

        damagedPlayer.setWalkSpeed(newSpeed);
        double duration = getDuration();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            damagedPlayer.setWalkSpeed(INIT_WALKING_SPEED);
        }, (long) (duration * 20) );

    }

    @Override
    public double getDuration() {
        return 7;
    }

    @Override
    public List<String> getDurationLorePart() {
        return null;
    }

    private String getAttributeModifierName(){
        return "crippling_shot";
    }

    private AttributeModifier getAttributeModifier(double subtractive){
        return new AttributeModifier(UUID.randomUUID(), "crippling_shot", subtractive, AttributeModifier.Operation.ADD_NUMBER);
    }

}
