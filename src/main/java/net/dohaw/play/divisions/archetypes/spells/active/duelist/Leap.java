package net.dohaw.play.divisions.archetypes.spells.active.duelist;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Damageable;
import net.dohaw.play.divisions.archetypes.spells.Rangeable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.archetypes.types.Duelist;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Leap extends ActiveSpell implements Damageable, Listener, Rangeable {

    final double BASE_DMG;
    final double HEART_INCREASE_PER_STRENGTH;
    final double STRENGTH_INTERVAL;
    final double Y_LEVEL_INCREASE;
    final double VELOCITY_SCALE;
    final double RADIUS;
    final double DAMAGE_SCALE;

    public Leap(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);

        BASE_DMG = defaultConfig.getLeapBaseDamage();
        HEART_INCREASE_PER_STRENGTH = defaultConfig.getLeapHeartIncreasePerStrength();
        STRENGTH_INTERVAL = defaultConfig.getLeapStrengthInterval();
        Y_LEVEL_INCREASE = defaultConfig.getLeapYLevelIncrease();
        VELOCITY_SCALE = defaultConfig.getLeapVelocityScale();
        RADIUS = defaultConfig.getLeapRadius();
        DAMAGE_SCALE = defaultConfig.getLeapDamageScale();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void execute(PlayerData pd) {

        Player player = pd.getPlayer().getPlayer();

        final Block blk = player.getTargetBlock(null, 10);

        Vector vel = blk.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(.25 * VELOCITY_SCALE);
        vel.setY(Y_LEVEL_INCREASE);
        player.setVelocity(vel);

        ArchetypeWrapper archetype = pd.getArchetype();
        Duelist duelist = (Duelist) archetype;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            duelist.setInMiddleOfLeap(true);
            pd.setArchetype(duelist);
            plugin.getPlayerDataManager().updatePlayerData(pd);
        }, 1);

    }

    @EventHandler
    public void onPlayerLand(PlayerMoveEvent e){

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(uuid);

        if(pd.getArchetype() != null){

            if(pd.getArchetype().getKEY() == ArchetypeKey.DUELIST){

                Duelist duelist = (Duelist) pd.getArchetype();

                if(duelist.isInMiddleOfLeap() && player.isOnGround()){

                    duelist.setInMiddleOfLeap(false);
                    pd.setArchetype(duelist);
                    plugin.getPlayerDataManager().updatePlayerData(pd);

                    double totalStrength = Calculator.getTotalStat(pd, Stat.STRENGTH);

                    double baseDmg;
                    if(totalStrength != 0){
                        baseDmg = BASE_DMG + ((totalStrength / STRENGTH_INTERVAL) * HEART_INCREASE_PER_STRENGTH);
                    }else{
                        baseDmg = BASE_DMG;
                    }

                    List<Entity> entitiesAroundPlayer = player.getNearbyEntities(RADIUS, RADIUS, RADIUS);
                    for(Entity en : entitiesAroundPlayer){
                        if(en instanceof LivingEntity){

                            LivingEntity le = (LivingEntity) en;

                            double distanceFromPlayer = le.getLocation().distance(player.getLocation());
                            double exponent = (distanceFromPlayer * -1) + 1.25;
                            double dmgFromDistance = (Math.pow(5, exponent) + 2);
                            double finalDmg = (baseDmg + dmgFromDistance) * DAMAGE_SCALE;

                            if(plugin.getDefaultConfig().isInDebugMode()){
                                player.sendMessage("WHO: " + le.getName());
                                player.sendMessage("DISTANCE: " + distanceFromPlayer);
                                player.sendMessage("BASE DMG: " + baseDmg);
                                player.sendMessage("Damage from distance: " + dmgFromDistance);
                                player.sendMessage("Final Damage: " + finalDmg);
                            }

                            le.damage(finalDmg);

                        }
                    }

                    player.getWorld().spawnParticle(getLandingParticle(), player.getLocation(), 40, 2, 2, 2);

                }
            }

        }


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
        return 0.15;
    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
        return 0;
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("The base damage is " + BASE_DMG, "Damage increases by " + HEART_INCREASE_PER_STRENGTH + " every " + STRENGTH_INTERVAL + " strength");
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return null;
    }

    public Particle getLandingParticle(){
        return Particle.SOUL_FIRE_FLAME;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("You leap into battle. When you land on the ground, you do damage to those around you");
    }

    private boolean isOnBlock(Player player){

        Location playerLoc = player.getLocation();
        Location under = playerLoc.clone().subtract(0, 1, 0);
        return under.getBlock().getType() != Material.AIR;

    }

    @Override
    public double getRange() {
        return RADIUS;
    }

    @Override
    public List<String> getRangeLorePart() {
        return Arrays.asList(getRange() + " blocks ");
    }
}
