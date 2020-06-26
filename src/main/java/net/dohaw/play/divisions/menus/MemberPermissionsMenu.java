package net.dohaw.play.divisions.menus;

import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
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

public class MemberPermissionsMenu extends Menu implements Listener {

    public MemberPermissionsMenu(JavaPlugin plugin, PlayerData playerData) {
        super(plugin, playerData.getPlayerName() + " Permissions", 45);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        EnumHelper enumHelper = ((DivisionsPlugin)plugin).getCoreAPI().getEnumHelper();
        PlayerDataManager playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        EnumMap<Permission, Object> playerPermissions = playerDataManager.getPlayerByUUID(p.getUniqueId()).getPermissions();

        int index = 0;
        for(Map.Entry<Permission, Object> permission : playerPermissions.entrySet()){

            String permissionName = enumHelper.enumToName(permission.getKey());
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
            lore.add(permissionsDescription);
            inv.setItem(index, createGuiItem(Material.STAINED_GLASS_PANE, permissionName, lore));
            index++;
        }

        setVariant((byte)15);
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

        if(clickedItem.getType().equals(Material.REDSTONE_TORCH_ON)){
            MembersMenu membersMenu = new MembersMenu(plugin, player);
            membersMenu.initializeItems(player);
            player.closeInventory();
            membersMenu.openInventory(player);
        }else if(clickedItem.getType().equals(Material.STAINED_GLASS_PANE)){
            Bukkit.broadcastMessage(clickedItem.getData()+"");
            if(clickedItem.getData().getData() != 15){
                Bukkit.broadcastMessage("yuh");
            }
        }
    }

}
