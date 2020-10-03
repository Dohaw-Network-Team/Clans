package net.dohaw.play.divisions.menus.itemcreation;

import net.dohaw.play.corelib.helpers.EnumHelper;
import net.dohaw.play.corelib.menus.Menu;
import net.dohaw.play.divisions.customitems.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ItemTypeFiltersMenu extends Menu implements Listener {

    public ItemTypeFiltersMenu(JavaPlugin plugin, Menu previousMenu) {
        super(plugin, previousMenu, "Item Type Filters", 36);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(10, createGuiItem(Material.IRON_HELMET, "&eArmor", new ArrayList<>()));
        inv.setItem(12, createGuiItem(Material.PORKCHOP, "&eConsumable", new ArrayList<>()));
        inv.setItem(14, createGuiItem(Material.DIAMOND, "&eGemstone", new ArrayList<>()));
        inv.setItem(16, createGuiItem(Material.DIAMOND_SWORD, "&eWeapon", new ArrayList<>()));

        setBackMaterial(Material.ARROW);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(true);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int slotClicked = e.getSlot();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        ItemType itemType = null;
        switch(slotClicked){
            case 10:
                itemType = ItemType.ARMOR;
                break;
            case 12:
                itemType = ItemType.CONSUMABLE;
                break;
            case 14:
                itemType = ItemType.GEMSTONE;
                break;
            case 16:
                itemType = ItemType.WEAPON;
                break;
        }

        if(itemType != null){
            ItemDisplayMenu newMenu = new ItemDisplayMenu(plugin, this, EnumHelper.enumToName(itemType), ItemFilter.ITEM_TYPES, itemType, 0);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }else if(backMat == clickedItem.getType()){
            goToPreviousMenu(player);
        }

    }
}
