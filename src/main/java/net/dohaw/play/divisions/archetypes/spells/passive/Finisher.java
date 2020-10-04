package net.dohaw.play.divisions.archetypes.spells.passive;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class Finisher extends PassiveSpell implements Listener {

    final double PERCENTAGE_THRESHOLD = 0.30;
    final double PERCENTAGE_DAMAGE_INCREASE = 0.30;

    public Finisher(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(archetype, KEY, levelUnlocked);
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        return Particle.CRIT;
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        Entity eDamager = e.getDamager();
        if(eDamager instanceof Player){

            Player damager = (Player) eDamager;
            PlayerData pd = plugin.getPlayerDataManager().getPlayerByUUID(damager.getUniqueId());

            if(pd.getArchetype().getKEY() == ArchetypeKey.DUELIST){

                Entity eDamaged = e.getEntity();
                if(eDamaged instanceof LivingEntity){

                    LivingEntity le = (LivingEntity) eDamaged;
                    AttributeInstance attr = le.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    double maxHP = attr.getValue();
                    double threshold = maxHP * PERCENTAGE_THRESHOLD;
                    double initDmg = e.getFinalDamage();

                    double currentHP = le.getHealth();
                    if(currentHP <= threshold){

                        double additive = PERCENTAGE_DAMAGE_INCREASE * initDmg;
                        double finalDmg = initDmg + additive;
                        e.setDamage(finalDmg);

                        if(plugin.getDefaultConfig().isInDebugMode()){
                            plugin.getLogger().info("FINISHER LISTENER");
                            plugin.getLogger().info("INIT DMG: " + initDmg);
                            plugin.getLogger().info("FINISHER LISTENER: " + finalDmg);
                        }

                        le.getWorld().spawnParticle(getSpellAffecterParticle(), le.getLocation(), 40, 2, 2, 2);

                    }

                }

            }

        }

    }

}
