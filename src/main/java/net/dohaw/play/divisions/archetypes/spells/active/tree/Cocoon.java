package net.dohaw.play.divisions.archetypes.spells.active.tree;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.*;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Cocoon extends ActiveSpell implements Damageable, Rangeable, Affectable, Healable {

    final int BASE_DMG = 2;
    final int NUM_LOGS_NEEDED_TO_MINE = 4;

    public Cocoon(String customItemBindedToKey, ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public double getDuration() {
        return 7;
    }

    @Override
    public List<String> getDurationLorePart() {
        return Collections.singletonList(getDuration() + " morphing seconds");
    }

    @Override
    public void execute(PlayerData pd) {

        Player player = pd.getPlayer().getPlayer();

        player.setGameMode(GameMode.SPECTATOR);

        Location initPlayerLocation = player.getLocation().clone();
        List<Location> mineableLogLocations = getTreeLogLocations(player.getLocation());
        Map<Location, Material> treeBlockLocations = spawnTree(mineableLogLocations);

        BukkitTask treeWatcher = new TreeWatcher(player, initPlayerLocation, mineableLogLocations, treeBlockLocations).runTaskTimer(plugin, 0L, 5L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            if(!treeWatcher.isCancelled()){

                TreeWatcher.removeTree(treeBlockLocations);
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(initPlayerLocation);
                treeWatcher.cancel();
                /*
                    Damages players that are within the radius
                 */
                double radius = getRange();
                List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
                for(Entity e : nearbyEntities){

                    if(e instanceof LivingEntity){

                        LivingEntity le = (LivingEntity) e;
                        double dmg = BASE_DMG;
                        if(le instanceof Player){
                            PlayerData playerInRadiusData = plugin.getPlayerDataManager().getPlayerByUUID(le.getUniqueId());
                            dmg = alterDamage(dmg, pd, playerInRadiusData);
                        }

                        /*
                            Could be less than 0 depending on how much fortitude their opponent has
                         */
                        if(dmg > 0){
                            le.damage(dmg);
                        }

                        le.getWorld().spawnParticle(getSpellAffecterParticle(), le.getLocation(), 40, 1, 1, 1);
                    }

                }

            }

        }, (long) (getDuration() * 20));

    }

    @Override
    public double getBaseCooldown() {
        return 120;
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public List<String> getCooldownLorePart(PlayerData pd) {
        return Collections.singletonList(getBaseCooldown() / 60 + " minute(s)");
    }

    @Override
    public List<String> getDamageLorePart() {
        return Arrays.asList("Does a base damage of " + BASE_DMG + " health points", "Is increased by Spell Power");
    }

    @Override
    public double getRange() {
        return 10;
    }

    @Override
    public List<String> getRangeLorePart() {
        return Collections.singletonList("Players have to be within " + getRange() + " blocks of your tree to take damage!");
    }

    @Override
    public double getPercentageRegenAffected() {
        return .4;
    }

    @Override
    public double alterDamage(double dmg, PlayerData spellOwner, PlayerData playerAffected) {
        double newDmg = Calculator.factorInSpellPower(spellOwner, dmg);
        newDmg = Calculator.factorInFortitude(playerAffected, newDmg);
        return newDmg;
    }

    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.HEART;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return Particle.SWEEP_ATTACK;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("You morph into a tree temporarily", "If your opponents don't break the logs of the tree when the spell duration ends, they will take damage", "You will also be healed");
    }

    @Override
    public double getPercentageHealthHealed() {
        return .4;
    }

    @Override
    public List<String> getHealingLorePart() {
        double percentage = getPercentageHealthHealed() * 100;
        return Collections.singletonList("Heals you for " + percentage + "% of your max HP");
    }

    /*
        Returns the location that the logs needed to mine should be.
     */
    public List<Location> getTreeLogLocations(Location playerLocation){

        List<Location> treeLogLocations = new ArrayList<>();

        for(double y = 0; y < NUM_LOGS_NEEDED_TO_MINE; y++){
            Location treeLogLocation = playerLocation.clone().add(0, y, 0);
            treeLogLocations.add(treeLogLocation);
        }

        return treeLogLocations;

    }

    /*
        Spawns the tree. Also returns the locations of the blocks that were in the tree's position so that I can revert changes of the world.
     */
    public Map<Location, Material> spawnTree(List<Location> treeLogLocations){

        /*
            Deals with the logs of the tree
         */
        Map<Location, Material> previousBlocks = new HashMap<>();

        for(Location logLoc : treeLogLocations){
            Block logPreviousBlock = logLoc.getBlock();
            previousBlocks.put(logLoc, logPreviousBlock.getType());
            logLoc.getBlock().setType(Material.JUNGLE_LOG);
        }

        /*
            Deals with the leaves of the tree
         */
        Location topLogLocation = treeLogLocations.get(treeLogLocations.size() - 1);
        List<Location> leafLocations = new ArrayList<Location>(){{
            add(topLogLocation.clone().add(0, 1,0));
            add(topLogLocation.clone().add(0, 0, 1));
            add(topLogLocation.clone().add(0, 0, -1));
            add(topLogLocation.clone().add(1, 0, 0));
            add(topLogLocation.clone().add(-1, 0, 0));
        }};

        for(Location leafLoc : leafLocations){
            Block leafPreviousBlock = leafLoc.getBlock();
            previousBlocks.put(leafLoc, leafPreviousBlock.getType());
            leafLoc.getBlock().setType(Material.JUNGLE_LEAVES);
        }

        return previousBlocks;

    }



}
