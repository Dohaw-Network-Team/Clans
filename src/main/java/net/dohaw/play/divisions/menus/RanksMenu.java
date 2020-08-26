package net.dohaw.play.divisions.menus;

import me.c10coding.coreapi.APIHook;
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

public class RanksMenu extends Menu implements Listener {

    public RanksMenu(JavaPlugin plugin) {
        super((APIHook) plugin, "Rank Permissions", 9);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        EnumHelper enumHelper = plugin.getAPI().getEnumHelper();
        ChatFactory chatFactory = plugin.getAPI().getChatFactory();
        int index = 0;
        for(Rank rank : Rank.values()){
            String displayName = chatFactory.colorString("&e" + enumHelper.enumToName(rank));
            inv.setItem(index, createGuiItem(Material.BOOKSHELF, displayName, new ArrayList<>()));
            index++;
        }

        setVariant((byte)15);
        setFillerMaterial(Material.STAINED_GLASS_PANE);
        setBackMaterial(Material.LEVER);
        fillMenu(true);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        e.setCancelled(true);
        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        if(clickedItem.getType().equals(backMat)){
            PermissionsMenu permissionsMenu = new PermissionsMenu(plugin);
            permissionsMenu.initializeItems(player);
            player.closeInventory();
            permissionsMenu.openInventory(player);
        }else if(clickedItem.getType().equals(Material.BOOKSHELF)){
            ChatFactory chatFactory = plugin.getAPI().getChatFactory();
            RankPermissionsMenu rankPermissionsMenu = new RankPermissionsMenu(plugin, chatFactory.removeChatColor(clickedItem.getItemMeta().getDisplayName()));
            rankPermissionsMenu.initializeItems(player);
            player.closeInventory();
            rankPermissionsMenu.openInventory(player);
        }

    }


}
