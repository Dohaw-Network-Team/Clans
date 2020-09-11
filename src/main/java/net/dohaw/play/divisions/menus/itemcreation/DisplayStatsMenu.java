package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.Stat;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.prompts.itemcreation.ItemCreationSessionPrompt;
import net.dohaw.play.divisions.prompts.itemcreation.StatLevelPrompt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayStatsMenu extends Menu implements Listener {

    private ItemCreationSession session;
    private EnumHelper enumHelper;

    public DisplayStatsMenu(APIHook plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "Edit Stats", 54);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.session = session;
        this.enumHelper = plugin.getAPI().getEnumHelper();
    }

    @Override
    public void initializeItems(Player p) {

        Map<Stat, Double> stats = session.getAddedStats();

        for(Map.Entry<Stat, Double> entry : stats.entrySet()){

            Stat stat = entry.getKey();
            double level = entry.getValue();

            int menuSlot = stat.getMenuSlot();
            Material menuMat = stat.getMenuMat();
            String displayName = enumHelper.enumToConfigKey(stat);
            List<String> lore = new ArrayList<String>(){{
                add("&cValue: &e" + level);
            }};

            inv.setItem(menuSlot, createGuiItem(menuMat, displayName, lore));

        }

        setBackMaterial(Material.ARROW);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(true);

    }

    public void setSession(ItemCreationSession session){
        this.session = session;
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

        if(slotClicked == (inv.getSize() - 1)){
            goToPreviousMenu(player);
        }else if(Stat.getStatByMenuMat(clickedItem.getType()) != null){

            Stat statClickedOn = Stat.getStatByMenuMat(clickedItem.getType());

            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new StatLevelPrompt(statClickedOn, this, session, ((DivisionsPlugin)plugin).getPlayerDataManager() )).withLocalEcho(false).buildConversation(player);
            conv.begin();
            player.closeInventory();

        }



    }
}
