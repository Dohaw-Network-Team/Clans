package net.dohaw.play.divisions.menus;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.rank.Permission;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RankPermissionsMenu extends Menu implements Listener {

    final private String rankName;

    public RankPermissionsMenu(JavaPlugin plugin, final String rankName) {
        super(plugin, rankName + " Permissions", 45);
        this.rankName = rankName;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        EnumHelper enumHelper = ((DivisionsPlugin)plugin).getCoreAPI().getEnumHelper();
        ChatFactory chatFactory = ((DivisionsPlugin)plugin).getCoreAPI().getChatFactory();
        DivisionsManager divisionsManager = ((DivisionsPlugin)plugin).getDivisionsManager();
        PlayerDataManager playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        Division division = divisionsManager.getDivision(playerDataManager.getPlayerByUUID(p.getUniqueId()).getDivision());
        EnumMap<Permission, Object> rankPermissions = division.getRankPermissions().get(enumHelper.nameToEnum(Rank.class, rankName));

        int index = 0;
        for(Map.Entry<Permission, Object> permission : rankPermissions.entrySet()){
            String permissionName = chatFactory.firstUpperRestLower(enumHelper.enumToName(permission.getKey()));
            String permissionsDescription = permission.getKey().getDescription();
            List<String> lore = new ArrayList<>();

            Object permissionValue = permission.getValue();
            setVariant((byte)0);
            if(permissionValue instanceof Boolean){
                boolean permissionBoolean = (Boolean) permissionValue;
                if(permissionBoolean){
                    setVariant((byte)5);
                    lore.add("&aTrue");
                }else{
                    setVariant((byte)14);
                    lore.add("&cFalse");
                }
            }else{
                setVariant((byte)9);
                if(permission.getKey().equals(Permission.GOLD_TAKE_AMOUNT)){
                    lore.add("&cAmount: &e" + permissionValue + " Gold");
                }else if(permission.getKey().equals(Permission.ITEM_TAKE_AMOUNT)){
                    lore.add("&cAmount: &e" + permissionValue + " Items");
                }
            }

            lore.add(" ");
            lore.add("&b" + permissionsDescription);

            inv.setItem(index, createGuiItem(Material.STAINED_GLASS_PANE, chatFactory.colorString(permissionName), chatFactory.colorLore(lore)));
            index++;
        }
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(fillerMat)) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
    }
}
