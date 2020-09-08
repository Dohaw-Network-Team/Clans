package net.dohaw.play.divisions;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class CustomItem {

    @Getter @Setter private String displayName;
    @Getter @Setter private Material material;
    @Getter @Setter private boolean isSpellItem;
    @Getter @Setter private Map<Enchantment, Integer> enchants = new HashMap<>();

    public CustomItem(Material material, String displayName){
        this.material = material;
        this.displayName = displayName;
    }

    public ItemStack getItem(){

        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        return null;


    }

}
