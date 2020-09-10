package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.customitems.ItemType;
import net.dohaw.play.divisions.customitems.Rarity;
import net.dohaw.play.divisions.managers.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemDisplayMenu extends Menu implements Listener {

    private final ItemFilter FILTER_CATEGORY;
    private final Enum FILTER;
    private CustomItemManager customItemManager;

    private List<CustomItem> allCustomItems = new ArrayList<>();
    private List<CustomItem> thisPageCustomItems = new ArrayList<>();

    private final Material NEXT_PAGE_MAT = Material.STRING;
    private final Material LAST_PAGE_MAT = Material.LEVER;
    private final Material BACK_MAT = Material.ARROW;

    private int pageNum;

    public ItemDisplayMenu(APIHook plugin, Menu previousMenu, String menuTitle, final ItemFilter FILTER_CATEGORY, final Enum FILTER, int pageNum) {
        super(plugin, previousMenu, menuTitle, 54);
        this.FILTER_CATEGORY = FILTER_CATEGORY;
        this.FILTER = FILTER;
        this.customItemManager = ((DivisionsPlugin)plugin).getCustomItemManager();
        this.pageNum = pageNum;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        if(FILTER_CATEGORY != null){
            if (FILTER_CATEGORY == ItemFilter.ITEM_TYPES) {
                allCustomItems = customItemManager.getByItemType((ItemType) FILTER);
            } else if (FILTER_CATEGORY == ItemFilter.SPELL_ITEMS) {
                allCustomItems = customItemManager.getSpellItems();
            } else{
                //Can't be anything other than Rarity
                allCustomItems = customItemManager.getByRarity((Rarity) FILTER);
            }
        }else{
            allCustomItems = (List<CustomItem>) customItemManager.getContents();
        }

        if(!allCustomItems.isEmpty()){

            int startingIndex = pageNum * inv.getSize();
            for(int x = startingIndex; x < allCustomItems.size(); x++){
                thisPageCustomItems.add(allCustomItems.get(x));
            }

            for(CustomItem ci : thisPageCustomItems){

                String displayName = ci.getDisplayName();
                Material material = ci.getMaterial();
                List<String> lore = ci.getLore();

                inv.addItem(createGuiItem(material, displayName, lore));

            }

        }

        setBackMaterial(BACK_MAT);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        inv.setItem(inv.getSize() - 5, createBackButton());
        fillMenu(false);

        createBackPageButton();
        createNextPageButton();

    }

    private void createNextPageButton(){
        inv.setItem(inv.getSize() - 1, createGuiItem(NEXT_PAGE_MAT, "&eNext Page", new ArrayList<>()));
    }

    private void createBackPageButton(){
        inv.setItem(inv.getSize() - 9, createGuiItem(LAST_PAGE_MAT, "&ePrevious Page", new ArrayList<>()));
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

        int backPageSLot = inv.getSize() - 9;
        int backSlot = inv.getSize() - 5;
        int nextPageSlot = inv.getSize() - 1;


        if(slotClicked == backSlot){
            player.closeInventory();
            previousMenu.openInventory(player);
        }

    }
}
