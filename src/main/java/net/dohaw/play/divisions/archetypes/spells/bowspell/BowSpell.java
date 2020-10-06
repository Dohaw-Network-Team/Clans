package net.dohaw.play.divisions.archetypes.spells.bowspell;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Affectable;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.RegenAffectable;
import net.dohaw.play.divisions.archetypes.spells.SpellKey;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;

public abstract class BowSpell extends PassiveSpell implements Listener, Cooldownable, RegenAffectable, Affectable {

    public BowSpell(ArchetypeWrapper archetype, SpellKey KEY, int levelUnlocked) {
        super(archetype, KEY, levelUnlocked);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public abstract void affectHitEntity(Entity eDamageTaker, PlayerData damagerDealer);

}
