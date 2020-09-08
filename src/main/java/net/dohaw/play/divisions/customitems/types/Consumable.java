package net.dohaw.play.divisions.customitems.types;

import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.customitems.ItemType;
import org.bukkit.Material;

public class Consumable extends CustomItem {

    public Consumable(ItemType itemType, Material material, String displayName) {
        super(itemType, material, displayName);
    }

}
