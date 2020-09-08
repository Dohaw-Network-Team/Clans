package net.dohaw.play.divisions.customitems.types;

import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.customitems.ItemType;
import org.bukkit.Material;

public class Weapon extends CustomItem {
    public Weapon(ItemType itemType, Material material, String displayName) {
        super(itemType, material, displayName);
    }
}
