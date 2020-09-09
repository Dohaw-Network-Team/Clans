package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.customitems.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemTypeFiltersMenu extends Menu implements Listener {

    private EnumHelper enumHelper;

    public ItemTypeFiltersMenu(APIHook plugin, Menu previousMenu) {
        super(plugin, previousMenu, "Item Type Filters", 36);
        this.enumHelper = plugin.getAPI().getEnumHelper();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(10, createGuiItem(Material.IRON_HELMET, "Armor", new ArrayList<>()));
        inv.setItem(12, createGuiItem(Material.PORKCHOP, "Consumable", new ArrayList<>()));
        inv.setItem(14, createGuiItem(Material.DIAMOND, "Gemstone", new ArrayList<>()));
        inv.setItem(16, createGuiItem(Material.DIAMOND_SWORD, "Weapon", new ArrayList<>()));

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
            ItemDisplayMenu newMenu = new ItemDisplayMenu(plugin, this, enumHelper.enumToName(itemType), ItemFilter.ITEM_TYPES, itemType);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }else if(backMat == clickedItem.getType()){
            goToPreviousMenu(player);
        }

    }
}
