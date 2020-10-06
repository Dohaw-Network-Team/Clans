package net.dohaw.play.divisions.menus.permissions;

import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.helpers.EnumHelper;
import net.dohaw.corelib.menus.Menu;
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

    /*
        Making previous menu null because it works right now without the previousMenu object
     */

    public RanksMenu(JavaPlugin plugin) {
        super(plugin, null, "Rank Permissions", 9);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        int index = 0;
        for(Rank rank : Rank.values()){
            String displayName = StringUtils.colorString("&e" + EnumHelper.enumToName(rank));
            inv.setItem(index, createGuiItem(Material.BOOKSHELF, displayName, new ArrayList<>()));
            index++;
        }

        setVariant((byte)15);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.LEVER);
        fillMenu(true);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        if(clickedItem.getType().equals(backMat)){
            PermissionsMenu permissionsMenu = new PermissionsMenu(plugin);
            permissionsMenu.initializeItems(player);
            player.closeInventory();
            permissionsMenu.openInventory(player);
        }else if(clickedItem.getType().equals(Material.BOOKSHELF)){
            RankPermissionsMenu rankPermissionsMenu = new RankPermissionsMenu(plugin, StringUtils.removeChatColor(clickedItem.getItemMeta().getDisplayName()));
            rankPermissionsMenu.initializeItems(player);
            player.closeInventory();
            rankPermissionsMenu.openInventory(player);
        }

    }


}
