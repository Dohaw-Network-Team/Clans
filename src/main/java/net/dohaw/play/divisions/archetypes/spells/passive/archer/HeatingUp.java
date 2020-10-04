package net.dohaw.play.divisions.archetypes.spells.passive.archer;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import net.dohaw.play.divisions.archetypes.types.Archer;
import net.dohaw.play.divisions.events.custom.HeatingUpCriticalStrikeEvent;
import net.dohaw.play.divisions.utils.Calculator;
import net.dohaw.play.divisions.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HeatingUp extends PassiveSpell implements Listener {

    public HeatingUp(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(archetype, KEY, levelUnlocked);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @Override
    public Particle getSpellOwnerParticle() {
        return Particle.DRIP_LAVA;
    }

    @Override
    public Particle getSpellAffecterParticle() {
        return Particle.EXPLOSION_NORMAL;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("For every 3 arrows you hit someone, you do extra damage on the third hit", "The extra amount of damage is based on your total luck and player level");
    }

    @EventHandler
    public void onSpellTrigger(EntityDamageByEntityEvent e) {

        double newDmg = e.getDamage();

        Entity eDamager = e.getDamager();
        if(EntityUtils.isEntityInvolvedAPlayer(eDamager)){
            if(eDamager instanceof Arrow){

                Player damager = (Player) EntityUtils.getPotentialPlayerFromProjectile(eDamager);
                UUID damagerUUID = damager.getUniqueId();
                PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(damagerUUID);
                World world = damager.getWorld();

                if(pd.getArchetype() instanceof Archer){
                    Archer archer = (Archer) pd.getArchetype();
                    int heatingUpCount = archer.getHeatingUpCount();
                    if(heatingUpCount == 2){

                        archer.setHeatingUpCount(0);
                        pd.setArchetype(archer);
                        plugin.getPlayerDataManager().updatePlayerData(pd);

                        if(getSpellAffecterParticle() != null){
                            Entity damaged = e.getEntity();
                            world.spawnParticle(getSpellAffecterParticle(), damaged.getLocation(), 40);
                        }

                        newDmg = alterDamage(newDmg, pd);
                        // Made this thinking I'd need it. I'm not actually utilizing it right now but I'll keep it here just in case I want to use it later.
                        Bukkit.getPluginManager().callEvent(new HeatingUpCriticalStrikeEvent(pd));

                        if(defaultConfig.isInDebugMode()){
                            Bukkit.broadcastMessage("Heat Up Critical Damage: " + newDmg);
                            Bukkit.broadcastMessage("Regular Damage: " + e.getDamage());
                        }
                        e.setDamage(newDmg);

                    }else{

                        if(getSpellOwnerParticle() != null){
                            world.spawnParticle(getSpellOwnerParticle(), damager.getLocation(), 40);
                        }

                        archer.setHeatingUpCount(heatingUpCount + 1);
                        pd.setArchetype(archer);
                        plugin.getPlayerDataManager().updatePlayerData(pd);

                    }
                }

            }
        }

    }

    @Override
    public double alterDamage(double dmg, PlayerData pd) {
        double totalLuck = Calculator.getTotalStat(pd, Stat.LUCK);
        double totalLuckMultiplier = defaultConfig.getHeatUpLuckMultiple();
        int playerLevel = pd.getLevel();
        return dmg + (totalLuckMultiplier * totalLuck) + (playerLevel - 1);
    }

}
