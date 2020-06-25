package net.dohaw.play.divisions.menus;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PermissionsMenu extends Menu implements Listener {

    private EnumHelper enumHelper;
    private ChatFactory chatFactory;

    public PermissionsMenu(JavaPlugin plugin) {
        super(plugin, "Permissions", 45);
        this.enumHelper = ((DivisionsPlugin)plugin).getCoreAPI().getEnumHelper();
        this.chatFactory = ((DivisionsPlugin)plugin).getCoreAPI().getChatFactory();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.broadcastMessage("Testing");
    }

    @Override
    public void initializeItems(Player p) {
        List<String> lore = new ArrayList<>();
        for(Rank rank : Rank.values()){
            lore.add(chatFactory.colorString("&f- &e" + enumHelper.enumToName(rank)));
        }
        inv.setItem(20, createGuiItem(Material.BOOKSHELF, chatFactory.colorString("&cRanks"), lore));
        inv.setItem(24, createGuiItem(Material.SKULL_ITEM, chatFactory.colorString("&cPlayers"), new ArrayList<>()));
        setVariant((byte)15);
        fillMenu(false);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        int slotClicked = e.getSlot();
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(fillerMat)) return;
        if(!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();

        switch(slotClicked){
            case 20:
                RanksMenu rankPermissionsMenu = new RanksMenu(plugin);
                rankPermissionsMenu.initializeItems(player);
                player.closeInventory();
                rankPermissionsMenu.openInventory(player);
                break;
            case 24:
                MembersMenu memberPermissionsMenu = new MembersMenu(plugin, player);
                memberPermissionsMenu.initializeItems(player);
                player.closeInventory();
                memberPermissionsMenu.openInventory(player);
                break;
            default:
                return;
        }


    }
}
