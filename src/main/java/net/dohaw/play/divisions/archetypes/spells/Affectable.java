package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.PlayerData;

public interface Affectable {

    void removeAffect(PlayerData pd);
    double getDuration();

}
