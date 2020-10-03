package net.dohaw.play.divisions.archetypes.spells.active;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface ActiveHittableSpell {
    void executeHit(Entity hitEntity, Player hitter);
}
