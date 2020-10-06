package net.dohaw.play.divisions.menus.itemcreation;

import net.dohaw.corelib.ChatSender;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class EditItemMenu extends Menu implements Listener {

    private String keyEditing;
    private CustomItemManager customItemManager;

    public EditItemMenu(JavaPlugin plugin, Menu previousMenu, String keyEditing) {
        super(plugin, previousMenu, "Edit Item", 27);
        this.keyEditing = keyEditing;
        this.customItemManager = ((DivisionsPlugin)plugin).getCustomItemManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(13, createGuiItem(Material.BARRIER, "Delete Item", new ArrayList<>()));

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

        if(slotClicked == 13){
            customItemManager.deleteItem(keyEditing);
            player.closeInventory();
            ChatSender.sendPlayerMessage("You have permanently deleted a custom item with the key &e" + keyEditing + "!", false, player, null);
        }else{
            goToPreviousMenu(player);
        }

    }
}
