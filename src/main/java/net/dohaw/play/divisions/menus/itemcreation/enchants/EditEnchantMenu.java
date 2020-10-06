package net.dohaw.play.divisions.menus.itemcreation.enchants;


import net.dohaw.corelib.menus.Menu;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public class EditEnchantMenu extends Menu implements Listener {

    private Enchantment enchantmentEditing;
    private ItemCreationSession session;

    public EditEnchantMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session, Enchantment enchantmentEditing) {
        super(plugin, previousMenu, "Edit Enchant", 27);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.session = session;
        this.enchantmentEditing = enchantmentEditing;
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(13, createGuiItem(Material.BARRIER, "&eDelete Enchantment", new ArrayList<>()));

        setBackMaterial(Material.ARROW);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(true);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        if(slotClicked == 13){

            Map<Enchantment, Integer> enchants = session.getEnchants();
            enchants.remove(enchantmentEditing);
            session.setEnchants(enchants);
            ((DisplayEnchantsMenu)previousMenu).setSession(session);

            previousMenu.clearItems();
            previousMenu.initializeItems(player);
            goToPreviousMenu(player);

        }else if(clickedItem.getType() == backMat){
            goToPreviousMenu(player);
        }

    }
}
