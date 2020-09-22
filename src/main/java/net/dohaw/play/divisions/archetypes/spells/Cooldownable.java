package net.dohaw.play.divisions.archetypes.spells;

import java.util.List;

public interface Cooldownable {

    double getCooldown();
    boolean displayCooldownMessage();
    List<String> getCooldownLorePart();

}
