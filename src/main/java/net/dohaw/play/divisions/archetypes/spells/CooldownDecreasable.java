package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.PlayerData;

import java.util.List;

public interface CooldownDecreasable extends Cooldownable {

    double getAdjustedCooldown(PlayerData pd);

}
