package net.dohaw.play.divisions.archetypes.spells.bowspell;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
import net.dohaw.play.divisions.archetypes.spells.Cooldownable;
import net.dohaw.play.divisions.archetypes.spells.RegenAffectable;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class BowSpell extends PassiveSpell implements Listener, Cooldownable, RegenAffectable {

    public BowSpell(String customItemBindedToKey, ArchetypeWrapper archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public abstract void affectHitPlayer(PlayerData damageTaker, PlayerData damagerDealer);

}
