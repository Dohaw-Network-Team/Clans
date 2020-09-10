package net.dohaw.play.divisions.customitems;

import lombok.Getter;
import lombok.Setter;
import net.dohaw.play.divisions.Stat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCreationSession {

    @Getter @Setter private String displayName = "None";
    @Getter @Setter private Material material = Material.APPLE;
    @Getter @Setter private ItemType itemType = ItemType.CONSUMABLE;
    @Getter @Setter private Rarity rarity = Rarity.UNCOMMON;
    @Getter @Setter private String key = "none";
    @Getter @Setter private List<String> lore = new ArrayList<>();
    @Getter @Setter private Map<Enchantment, Integer> enchants = new HashMap<>();
    @Getter @Setter private Map<Stat, Double> addedStats = new HashMap<>();

    public ItemCreationSession(){}

    public CustomItem toItem(){
        CustomItem ci = new CustomItem(key, itemType, material, displayName);
        ci.setLore(lore);
        ci.setRarity(rarity);
        ci.setEnchants(enchants);
        ci.setAddedStats(addedStats);
        return ci;
    }

}
