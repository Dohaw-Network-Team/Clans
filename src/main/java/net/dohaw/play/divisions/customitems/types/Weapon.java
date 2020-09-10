package net.dohaw.play.divisions.customitems.types;

import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.customitems.ItemStackable;
import net.dohaw.play.divisions.customitems.ItemType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Weapon extends CustomItem implements ItemStackable {

    public Weapon(String KEY, ItemType itemType, Material material, String displayName) {
        super(KEY, itemType, material, displayName);
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }
}
