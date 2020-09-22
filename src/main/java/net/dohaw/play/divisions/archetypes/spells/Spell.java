package net.dohaw.play.divisions.archetypes.spells;

import lombok.Setter;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.*;
import net.dohaw.play.divisions.archetypes.spells.active.ActiveSpell;
import net.dohaw.play.divisions.archetypes.spells.active.FrostStrike;
import net.dohaw.play.divisions.archetypes.spells.active.InvisibleStrike;
import net.dohaw.play.divisions.archetypes.spells.active.Smite;
import net.dohaw.play.divisions.archetypes.spells.passive.PassiveSpell;
import net.dohaw.play.divisions.archetypes.spells.passive.archer.HeatingUp;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Spell extends WrapperHolder {

    public static final ActiveSpell INVISIBLE_STRIKE = new InvisibleStrike("invisible_strike_spell", Archetype.EVOKER, SpellKey.INVISIBLE_STRIKE, 1);
    public static final ActiveSpell SMITE = new Smite("smite_spell", Archetype.CLERIC, SpellKey.SMITE, 1);
    public static final ActiveSpell FROST_STRIKE = new FrostStrike("frost_strike", Archetype.WIZARD, SpellKey.FROST_STRIKE, 1);

    public static final PassiveSpell HEATING_UP = new HeatingUp("heating_up_spell", Archetype.ARCHER, SpellKey.HEATING_UP, 1);

    public static ActiveSpell getSpellByItemKey(String customItemKey) {
        for (Map.Entry<Enum, Wrapper> entry : wrappers.entrySet()) {
            Wrapper wrapper = entry.getValue();
            if (wrapper instanceof ActiveSpell) {
                ActiveSpell spell = (ActiveSpell) entry.getValue();
                if (spell.getCustomItemBindedToKey().equalsIgnoreCase(customItemKey)) {
                    return spell;
                }
            }
        }
        return null;
    }

    public static List<SpellWrapper> getArchetypeSpells(ArchetypeWrapper archetypeWrapper) {
        List<SpellWrapper> spells = new ArrayList<>();
        for (Map.Entry<Enum, Wrapper> entry : wrappers.entrySet()) {
            SpellWrapper spell = (SpellWrapper) entry.getValue();
            if (spell.getArchetype().getKEY() == archetypeWrapper.getKEY()) {
                spells.add(spell);
            }
        }
        return spells;
    }

    public static List<SpellWrapper> getArchetypeSpellsUnlocked(ArchetypeWrapper archetype, int level) {
        List<SpellWrapper> archetypeSpells = getArchetypeSpells(archetype);
        List<SpellWrapper> spellsUnlocked = new ArrayList<>();
        for (SpellWrapper wrapper : archetypeSpells) {
            if (wrapper.getLevelUnlocked() == level) {
                spellsUnlocked.add(wrapper);
            }
        }
        return spellsUnlocked;
    }

    public static boolean isSpellEntity(Entity entity) {
        return entity.hasMetadata("spell_key");
    }

    public static boolean hasEnoughRegen(PlayerData pd, ActiveSpell spell) {
        return pd.getRegen() >= Calculator.getSpellRegenCost(pd, spell);
    }

    public static Entity getEntityInSights(Player player, double range) {
        
        List<Entity> nearbyE = player.getNearbyEntities(range,
                range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        Entity target = null;
        BlockIterator bItr = new BlockIterator(player.getLocation(), range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {

            block = bItr.next();
            if(block.getType() != Material.AIR){
                bx = block.getX();
                by = block.getY();
                bz = block.getZ();
                // check for entities near this block in the line of sight
                for (LivingEntity e : livingE) {
                    loc = e.getLocation();
                    ex = loc.getX();
                    ey = loc.getY();
                    ez = loc.getZ();
                    if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75) && (by - 1 <= ey && ey <= by + 2.5)) {
                        // entity is close enough, set target and stop
                        target = e;
                        break;
                    }
                }
            }

        }

        return target;

    }

}
