package net.dohaw.play.divisions.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public class EnchantmentSerializer {

    public static Object[] toEnchantment(String line){
        String[] arr = line.split(";");
        int level = Integer.parseInt(arr[1]);
        Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(arr[0]));
        return new Object[]{ench, level};
    }

    public static String toLine(Map.Entry<Enchantment, Integer> entry){
        return entry.getKey().getKey().getKey() + ";" + entry.getValue();
    }

}
