package net.dohaw.play.divisions.archetypes.spells.active.tree;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Affectable;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import net.dohaw.play.divisions.archetypes.spells.Rangeable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Root extends ActiveSpell implements Affectable, Damageable, Rangeable {

    final double BASE_DMG = 1;

    public Root(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(PlayerData pd) {

        Player player = pd.getPlayer().getPlayer();

        player.getWorld().spawnParticle(getSpellOwnerParticle(), player.getLocation(), 40, 1, 1, 1);

        double range = getRange();
        List<Entity> nearbyEntities = player.getNearbyEntities(range, range, range);

        double damage = Calculator.factorInSpellPower(pd, BASE_DMG);
        for(Entity e : nearbyEntities){

            if(e instanceof LivingEntity){

                long duration = (long) (getDuration() * 20);
                LivingEntity le = (LivingEntity) e;
                if(le instanceof Player){

                    Player playerRooted = (Player) e;
                    playerRooted.setMetadata("rooted", new FixedMetadataValue(plugin, true));

                    Bukkit.getScheduler().runTaskLater(plugin, () ->{
                        playerRooted.removeMetadata("rooted", plugin);
                    }, duration);

                    PlayerData playerRootedData = plugin.getPlayerDataManager().getPlayerByUUID(playerRooted.getUniqueId());
                    damage = Calculator.factorInFortitude(playerRootedData, damage);

                    if(damage > 0){
                        playerRooted.damage(damage);
                    }

                }else{

                    le.setAI(false);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        le.setAI(true);
                    }, duration);

                }

                le.getWorld().spawnParticle(getSpellAffecterParticle(), le.getLocation() , 1, 1,1,40);

            }

        }

    }

    @Override
    public double getBaseCooldown() {
        return 25;
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public List<String> getCooldownLorePart(PlayerData pd) {
        return Collections.singletonList(getBaseCooldown() + " seconds");
    }

    @Override
    public double getPercentageRegenAffected() {
        return .10;
    }

    @Override
    public double alterDamage(double dmg, PlayerData spellOwner, PlayerData playerAffected) {
        return 0;
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("Has a base damage of " + BASE_DMG, "Damage can be increased by your spell power");
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.SPELL_WITCH;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return Particle.SPELL_INSTANT;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Roots nearby entities");
    }

    @Override
    public double getDuration() {
        return 4;
    }

    @Override
    public List<String> getDurationLorePart() {
        return Arrays.asList(getDuration() + " seconds");
    }

    @Override
    public double getRange() {
        return 10;
    }

    @Override
    public List<String> getRangeLorePart() {
        return Arrays.asList(getDuration() + " blocks");
    }
}
