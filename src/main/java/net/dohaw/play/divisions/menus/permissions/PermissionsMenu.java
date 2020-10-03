package net.dohaw.play.divisions.menus.permissions;

import net.dohaw.play.corelib.StringUtils;
import net.dohaw.play.corelib.helpers.EnumHelper;
import net.dohaw.play.corelib.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PermissionsMenu extends Menu implements Listener {

    /*
        Making previous menu null because it works right now without the previousMenu object
     */

    public PermissionsMenu(JavaPlugin plugin) {
        super(plugin, null,"Permissions", 45);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        List<String> lore = new ArrayList<>();
        for(Rank rank : Rank.values()){
            lore.add(StringUtils.colorString("&f- &e" + EnumHelper.enumToName(rank)));
        }

        ItemStack head = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(p.getName());
        skullMeta.setDisplayName(StringUtils.colorString("&cPlayers"));
        head.setItemMeta(skullMeta);

        inv.setItem(20, createGuiItem(Material.BOOKSHELF, StringUtils.colorString("&cRanks"), lore));
        inv.setItem(24, head);
        setVariant((byte)15);

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(false);
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
        }

    }

}
