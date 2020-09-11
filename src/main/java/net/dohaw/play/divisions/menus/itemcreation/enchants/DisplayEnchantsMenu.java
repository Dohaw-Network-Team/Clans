package net.dohaw.play.divisions.menus.itemcreation.enchants;

import lombok.Setter;
import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayEnchantsMenu extends Menu implements Listener {

    @Setter
    private ItemCreationSession session;
    private final Material ITEM_MAT = Material.NAME_TAG;
    private final Material ADD_ENCHANT_MAT = Material.SLIME_BLOCK;

    public DisplayEnchantsMenu(APIHook plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "Item Enchants", 45);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.session = session;
    }

    @Override
    public void initializeItems(Player p) {

        Map<Enchantment, Integer> enchants = session.getEnchants();
        for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()){

            Enchantment ench = entry.getKey();
            int level = entry.getValue();
            String enchName = "&e" + ench.getName();

            List<String> lore = new ArrayList<String>(){{
                add("Level: " + level);
            }};

            inv.addItem(createGuiItem(ITEM_MAT, enchName, lore));

        }

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

        if(clickedItem.getType() == ITEM_MAT){

            Map<Enchantment, Integer> enchants = session.getEnchants();
            Enchantment enchEditing = (Enchantment) enchants.keySet().toArray()[slotClicked];

            EditEnchantMenu newMenu = new EditEnchantMenu(plugin, this, session, enchEditing);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);

        }else if(clickedItem.getType() == backMat){
            goToPreviousMenu(player);
        }else if(clickedItem.getType() == ADD_ENCHANT_MAT){

        }

    }
}
