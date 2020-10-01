package net.dohaw.play.divisions.archetypes.spells.bowspell;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Affectable;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import net.dohaw.play.divisions.archetypes.spells.RegenAffectable;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CripplingShot extends BowSpell implements Damageable, RegenAffectable, Cooldownable, Affectable {

    public CripplingShot(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
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
    public void affectHitPlayer(PlayerData damageTaker, PlayerData damagerDealer) {

        Player damagedPlayer = damageTaker.getPlayer().getPlayer();
        AttributeInstance speedAttribute = damagedPlayer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

        double currentBaseValue = speedAttribute.getBaseValue();
        double subtractive = (currentBaseValue * .25) * -1;

        speedAttribute.addModifier(getAttributeModifier(subtractive));

    }

    @Override
    public void removeAffect(PlayerData pd) {
        OfflinePlayer op = pd.getPlayer();
    }

    @Override
    public double getDuration() {
        return 0;
    }

    private String getAttributeModifierName(){
        return "crippling_shot";
    }

    private AttributeModifier getAttributeModifier(double subtractive){
        return new AttributeModifier(UUID.randomUUID(), "crippling_shot", subtractive, AttributeModifier.Operation.ADD_NUMBER);
    }

}
