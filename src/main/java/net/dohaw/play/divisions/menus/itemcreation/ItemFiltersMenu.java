package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemFiltersMenu extends Menu implements Listener {

    public ItemFiltersMenu(APIHook plugin, Menu previousMenu) {
        super(plugin, previousMenu, "Custom Item Filters", 18);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.addItem(createGuiItem(Material.ACACIA_SAPLING, "&4All Items", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.BLAZE_POWDER, "&eSpell Items", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.GRASS_BLOCK, "&eBy Item Type", new ArrayList<>()));
        inv.addItem(createGuiItem(Material.DIAMOND, "&eBy Rarity", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);

    }

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
                newMenu = new ItemDisplayMenu(plugin, this, "All Items", null, null);
                break;
            case BLAZE_POWDER:
                newMenu = new ItemDisplayMenu(plugin, this,"Spell Items", ItemFilter.SPELL_ITEMS, null);
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
