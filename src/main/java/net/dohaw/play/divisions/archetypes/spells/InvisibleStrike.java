package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import net.dohaw.play.divisions.archetypes.RegenType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InvisibleStrike extends SpellWrapper{

    public InvisibleStrike(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public RegenType getRegenTypeAffected() {
        return RegenType.MANA;
    }

    @Override
    public void execute(Player player, boolean outOrIn) {
        Bukkit.broadcastMessage("Yo yo");
    }

    @Override
    public boolean displayCooldownMessage() {
        return false;
    }

    @Override
    public double alterDamage() {
        return 0;
    }

    @Override
    public double getCooldown() {
        return 0.5;
    }

    @Override
    public double getPercentageRegenAffected() {
        return 0.10;
    }


}
