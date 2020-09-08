package net.dohaw.play.divisions.menus.permissions;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.prompts.NumericPermissionPrompt;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RankPermissionsMenu extends Menu implements Listener {

    final private String rankName;
    private DivisionsManager divisionsManager;
    private PlayerDataManager playerDataManager;
    private EnumHelper enumHelper;
    private ChatFactory chatFactory;

    public RankPermissionsMenu(JavaPlugin plugin, final String rankName) {
        super((APIHook) plugin, rankName + " Permissions", 45);
        this.rankName = rankName;
        this.divisionsManager = ((DivisionsPlugin)plugin).getDivisionsManager();
        this.playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        this.enumHelper = ((DivisionsPlugin)plugin).getAPI().getEnumHelper();
        this.chatFactory = ((DivisionsPlugin)plugin).getAPI().getChatFactory();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        Division division = divisionsManager.getDivision(playerDataManager.getPlayerByUUID(p.getUniqueId()).getDivision());
        EnumMap<Permission, Object> rankPermissions;

        /*
            Short-term fix for the IllegalArgumentException that was given whenever going from Permissions Menu to here.
         */
        try{
            rankPermissions = division.getRankPermissions().get(enumHelper.nameToEnum(Rank.class, rankName));
        }catch(IllegalArgumentException e){
            return;
        }

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

            inv.setItem(index, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, chatFactory.colorString(permissionName), chatFactory.colorLore(lore)));
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
        int slotNum = e.getSlot();

        if(clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)){
            byte data = clickedItem.getData().getData();
            if(data == (byte)5 || data == (byte)14 || data == (byte)9){

                ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                List<String> lore = clickedItemMeta.getLore();
                Division division = divisionsManager.getDivision(playerDataManager.getPlayerByUUID(player.getUniqueId()).getDivision());
                Rank rank = (Rank) enumHelper.nameToEnum(Rank.class, rankName);
                String displayName = chatFactory.removeChatColor(clickedItem.getItemMeta().getDisplayName());
                displayName = displayName.replace(" ", "_");
                displayName = displayName.toUpperCase();
                Permission permission = Permission.valueOf(displayName);

                if(data == (byte)5){
                    clickedItem.setDurability((short)14);
                    lore.set(0, chatFactory.colorString("&cFalse"));
                    setPermission(division, rank, permission, false);
                }else if(data == (byte)14){
                    clickedItem.setDurability((byte)5);
                    lore.set(0, chatFactory.colorString("&aTrue"));
                    setPermission(division, rank, permission, true);
                }else{
                    player.closeInventory();
                    ConversationFactory cf = new ConversationFactory(plugin);
                    Conversation conv = cf.withFirstPrompt(new NumericPermissionPrompt((DivisionsPlugin) plugin, permission, division, rank)).withLocalEcho(false).buildConversation(player);
                    conv.begin();
                    return;
                }

                clickedItemMeta.setLore(lore);
                clickedItem.setItemMeta(clickedItemMeta);
                inv.setItem(slotNum, clickedItem);

            }
        }else if(clickedItem.getType().equals(backMat)){
            RanksMenu ranksMenu = new RanksMenu(plugin);
            ranksMenu.initializeItems(player);
            player.closeInventory();
            ranksMenu.openInventory(player);
        }

    }

    private void setPermission(Division division, Rank rank, Permission perm, Object obj){
        division.setRankPermission(rank, perm, obj);
        divisionsManager.updateDivision(division.getName(), division);
    }

}
