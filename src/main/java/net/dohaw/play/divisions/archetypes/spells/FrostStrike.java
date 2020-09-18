package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.archetypes.ArchetypeKey;
import org.bukkit.entity.Player;

public class FrostStrike extends SpellWrapper{

    public FrostStrike(String customItemBindedToKey, ArchetypeKey archetype, Enum KEY, int levelUnlocked) {
        super(customItemBindedToKey, archetype, KEY, levelUnlocked);
    }

    @Override
    public void execute(Player player) {

    }
}
