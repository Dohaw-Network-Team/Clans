package net.dohaw.play.divisions.archetypes.spells.passive.archer;

import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import net.dohaw.play.divisions.archetypes.types.Archer;
import net.dohaw.play.divisions.events.custom.HeatingUpCriticalStrikeEvent;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.utils.Calculator;
import net.dohaw.play.divisions.utils.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class HeatingUp extends PassiveSpell implements Listener {

    private DivisionsPlugin plugin;

    public HeatingUp(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
        this.plugin = DivisionsPlugin.getInstance();
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

    @EventHandler
    public void onSpellTrigger(EntityDamageByEntityEvent e) {

        PlayerDataManager pdManager = plugin.getPlayerDataManager();
        double newDmg = e.getDamage();

        Entity eDamager = e.getDamager();
        if(EntityUtils.isEntityInvolvedAPlayer(eDamager)){
            if(eDamager instanceof Arrow){

                Player damager = (Player) EntityUtils.getPotentialPlayerFromProjectile(eDamager);
                UUID damagerUUID = damager.getUniqueId();
                PlayerData pd = pdManager.getPlayerByUUID(damagerUUID);
                World world = damager.getWorld();

                if(pd.getArchetype() instanceof Archer){
                    Archer archer = (Archer) pd.getArchetype();
                    int heatingUpCount = archer.getHeatingUpCount();
                    if(heatingUpCount == 2){

                        archer.setHeatingUpCount(0);
                        pd.setArchetype(archer);
                        pdManager.updatePlayerData(damagerUUID, pd);

                        if(getSpellAffecterParticle() != null){
                            Entity damaged = e.getEntity();
                            world.spawnParticle(getSpellAffecterParticle(), damaged.getLocation(), 40);
                        }

                        newDmg = alterDamage(newDmg);
                        // Made this thinking I'd need it. I'm not actually utilizing it right now but I'll keep it here just in case I want to use it later.
                        Bukkit.getPluginManager().callEvent(new HeatingUpCriticalStrikeEvent(pd));

                    }else{

                        if(getSpellOwnerParticle() != null){
                            world.spawnParticle(getSpellOwnerParticle(), damager.getLocation(), 40);
                        }

                        archer.setHeatingUpCount(heatingUpCount + 1);
                        pd.setArchetype(archer);
                        pdManager.updatePlayerData(damagerUUID, pd);

                    }
                }

            }
        }

        e.setDamage(newDmg);

    }

    @Override
    public double getCooldown() {
        return 0;
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public double alterDamage(double dmg) {
        return 0;
    }

    @Override
    public double getRange() {
        return 0;
    }
}
