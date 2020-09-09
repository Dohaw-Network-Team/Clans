package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.managers.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ItemDisplayMenu extends Menu implements Listener {

    private final ItemFilter FILTER_CATEGORY;
    private final Enum FILTER;
    private int page;
    private CustomItemManager customItemManager;

    private Map<String, CustomItem> allCustomItems;
    private Map<String, CustomItem> thisPageCustomItems = new HashMap<>();

    private final Material NEXT_PAGE_MAT = Material.STRING;
    private final Material BACK_MAT = Material.ARROW;

    public ItemDisplayMenu(APIHook plugin, Menu previousMenu, String menuTitle, final ItemFilter FILTER_CATEGORY, final Enum FILTER) {
        super(plugin, previousMenu, menuTitle, 54);
        this.FILTER_CATEGORY = FILTER_CATEGORY;
        this.FILTER = FILTER;
        this.customItemManager = ((DivisionsPlugin)plugin).getCustomItemManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {



    }

    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
