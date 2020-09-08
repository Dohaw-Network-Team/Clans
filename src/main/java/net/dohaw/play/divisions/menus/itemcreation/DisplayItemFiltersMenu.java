package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class DisplayItemFiltersMenu extends Menu implements Listener {

    public DisplayItemFiltersMenu(APIHook plugin) {
        super(plugin, "Custom Item Filters", 18);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.addItem(createGuiItem(Material.BOOK, "&eBy Material", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.BLAZE_POWDER, "&eSpell Items", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.GRASS_BLOCK, "&eBy Item Type", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.DIAMOND, "&eBy Rarity", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(true);

    }

    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
