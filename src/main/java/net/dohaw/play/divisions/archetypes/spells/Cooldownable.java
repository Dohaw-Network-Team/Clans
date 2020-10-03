package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.PlayerData;

import java.util.List;

public interface Cooldownable {

    double getBaseCooldown();
    boolean displayCooldownMessage();
    List<String> getCooldownLorePart(PlayerData pd);

}
