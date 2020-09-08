package net.dohaw.play.divisions.customitems;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.Stat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomItem {

    @Getter private final String KEY;
    @Getter @Setter private String displayName;
    @Getter @Setter private List<String> lore;
    @Getter @Setter private Material material;
    @Getter @Setter private boolean isSpellItem;
    @Getter @Setter private ItemType itemType;
    @Getter @Setter private Rarity rarity;
    @Getter @Setter private Map<Enchantment, Integer> enchants = new HashMap<>();
    @Getter @Setter private Map<Stat, Double> addedStats = new HashMap<>();

    public CustomItem(final String KEY, ItemType itemType, Material material, String displayName){
        this.material = material;
        this.displayName = displayName;
        this.itemType = itemType;
        this.KEY = KEY;
    }

    public abstract ItemStack getItemStack();

}
