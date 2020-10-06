package net.dohaw.play.divisions.menus.itemcreation;

import net.dohaw.corelib.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ItemFiltersMenu extends Menu implements Listener {

    public ItemFiltersMenu(JavaPlugin plugin, Menu previousMenu) {
        super(plugin, previousMenu, "Custom Item Filters", 36);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(10, createGuiItem(Material.ACACIA_SAPLING, "&eAll Items", new ArrayList<>()));
        inv.setItem(12, createGuiItem(Material.BLAZE_POWDER, "&eSpell Items", new ArrayList<>()));
        inv.setItem(14, createGuiItem(Material.GRASS_BLOCK, "&eBy Item Type", new ArrayList<>()));
        inv.setItem(16, createGuiItem(Material.DIAMOND, "&eBy Rarity", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        Material matClicked = clickedItem.getType();
        Menu newMenu = null;
        switch(matClicked){
            case ACACIA_SAPLING:
                newMenu = new ItemDisplayMenu(plugin, this, "All Items", null, null, 0);
                break;
            case BLAZE_POWDER:
                newMenu = new ItemDisplayMenu(plugin, this,"Spell Items", ItemFilter.SPELL_ITEMS, null, 0);
                break;
            case GRASS_BLOCK:
                newMenu = new ItemTypeFiltersMenu(plugin, this);
                break;
            case DIAMOND:
                newMenu = new ItemRarityFiltersMenu(plugin, this);
                break;
        }

        if(newMenu != null){
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }else if(backMat == matClicked){
            goToPreviousMenu(player);
        }

    }
}
