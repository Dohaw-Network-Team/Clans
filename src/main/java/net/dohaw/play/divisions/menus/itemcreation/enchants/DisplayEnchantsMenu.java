package net.dohaw.play.divisions.menus.itemcreation.enchants;

import lombok.Setter;
import net.dohaw.play.corelib.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.menus.itemcreation.CreateItemMenu;
import net.dohaw.play.divisions.menus.itemcreation.CustomItemsMenu;
import net.dohaw.play.divisions.prompts.itemcreation.ItemCreationSessionPrompt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayEnchantsMenu extends Menu implements Listener {

    @Setter private ItemCreationSession session;
    private final Material ITEM_MAT = Material.NAME_TAG;
    private final Material ADD_ENCHANT_MAT = Material.SLIME_BLOCK;

    public DisplayEnchantsMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session) {
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
            String enchName = "&e" + ench.getKey().getKey();

            List<String> lore = new ArrayList<String>(){{
                add("&cLevel: &e" + level);
            }};

            inv.addItem(createGuiItem(ITEM_MAT, enchName, lore));

        }

        inv.setItem(inv.getSize() - 9, createGuiItem(ADD_ENCHANT_MAT, "&eAdd Enchantment", new ArrayList<>()));

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

            DivisionsPlugin divPlugin = (DivisionsPlugin) plugin;
            CustomItemManager cim = divPlugin.getCustomItemManager();

            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ItemCreationSessionPrompt(cim, ItemCreationSessionPrompt.Change.ENCHANTMENT, new CreateItemMenu(plugin, new CustomItemsMenu(plugin), session), session, ((DivisionsPlugin)plugin).getPlayerDataManager() )).withLocalEcho(false).buildConversation(player);
            conv.begin();
            player.closeInventory();

        }

    }
}
