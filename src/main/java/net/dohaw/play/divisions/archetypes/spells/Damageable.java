package net.dohaw.play.divisions.archetypes.spells;

import net.dohaw.play.divisions.PlayerData;

import java.util.List;

public interface Damageable {

    double alterDamage(double dmg, PlayerData spellOwner, PlayerData playerAffected);
    List<String> getDamageLorePart();

}
