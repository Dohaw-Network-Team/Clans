package net.dohaw.play.divisions.menus.itemcreation.lore;

import lombok.Setter;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.menus.itemcreation.CreateItemMenu;
import net.dohaw.play.divisions.menus.itemcreation.CustomItemsMenu;
import net.dohaw.play.divisions.prompts.itemcreation.ItemCreationSessionPrompt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DisplayLoreMenu extends Menu implements Listener {

    @Setter
    private ItemCreationSession session;
    private final Material ITEM_MAT = Material.PAPER;

    public DisplayLoreMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "Change Lore", 45);
        this.session = session;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        int num = 1;
        List<String> sessionLore = session.getLore();
        for(String line : sessionLore){
            String displayName = "&cLine &e" + num;
            List<String> itemLore = new ArrayList<>();
            itemLore.add(line);
            inv.addItem(createGuiItem(ITEM_MAT, displayName, itemLore));
            num++;
        }

        inv.setItem(inv.getSize() - 9, createGuiItem(Material.SLIME_BLOCK, "&eAdd Lore Line", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);

    }

    /*
    public void reloadItems(Player p){
        inv.clear();
        initializeItems(p);
    }*/

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

        int addLoreLineSlot = inv.getSize() - 9;

        if(clickedItem.getType() == ITEM_MAT){

            EditLoreLineMenu newMenu = new EditLoreLineMenu(plugin, this, session, slotClicked);

            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);

        }else if(clickedItem.getType() == backMat){
            goToPreviousMenu(player);
        }else if(slotClicked == addLoreLineSlot){

            DivisionsPlugin divPlugin = (DivisionsPlugin)plugin;

            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ItemCreationSessionPrompt(divPlugin.getCustomItemManager(), ItemCreationSessionPrompt.Change.ADD_LORE_LINE, new CreateItemMenu(plugin, new CustomItemsMenu(plugin), session), session, ((DivisionsPlugin)plugin).getPlayerDataManager() )).withLocalEcho(false).buildConversation(player);
            conv.begin();
            player.closeInventory();
        }

    }
}
