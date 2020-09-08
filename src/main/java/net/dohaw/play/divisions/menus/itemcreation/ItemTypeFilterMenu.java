package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class ItemTypeFilterMenu extends Menu implements Listener {

    public ItemTypeFilterMenu(APIHook plugin) {
        super(plugin, "Item Type Filters", 9);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.addItem(createGuiItem(Material.DIAMOND, "&bGemstones", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.IRON_CHESTPLATE, "&cArmor", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.GOLDEN_SWORD, "&eWeapons", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.PORKCHOP, "&aConsumables", new ArrayList<>()));

        fillMenu(true);

    }

    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
