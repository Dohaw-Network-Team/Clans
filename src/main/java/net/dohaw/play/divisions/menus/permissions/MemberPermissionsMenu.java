package net.dohaw.play.divisions.menus.permissions;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.EnumHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.prompts.NumericPermissionPrompt;
import net.dohaw.play.divisions.rank.Permission;
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

public class MemberPermissionsMenu extends Menu implements Listener {

    private ChatFactory chatFactory;
    private EnumHelper enumHelper;
    private PlayerDataManager playerDataManager;
    private PlayerData playerData;

    public MemberPermissionsMenu(JavaPlugin plugin, PlayerData playerData) {
        super((APIHook) plugin, playerData.getPLAYER_NAME() + " Permissions", 45);
        this.chatFactory = ((DivisionsPlugin)plugin).getAPI().getChatFactory();
        this.enumHelper = ((DivisionsPlugin)plugin).getAPI().getEnumHelper();
        this.playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        this.playerData = playerData;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        EnumMap<Permission, Object> playerPermissions = playerData.getPermissions();

        int index = 0;
        for(Permission perm : Permission.values()){

            List<String> lore = new ArrayList<>();
            String permissionDescription = perm.getDescription();
            String permissionName = chatFactory.firstUpperRestLower(enumHelper.enumToName(perm));

            if(playerPermissions.get(perm) != null){

                Object permValue = playerPermissions.get(perm);
                if(permValue instanceof String){
                    setVariant((byte)2);
                    lore.add("&dNot set");
                }else if(permValue instanceof Boolean) {
                    boolean permissionBoolean = (Boolean) permValue;
                    if (permissionBoolean) {
                        setVariant((byte) 5);
                        lore.add("&aTrue");
                    } else {
                        setVariant((byte) 14);
                        lore.add("&cFalse");
                    }
                }else{
                    setVariant((byte)9);
                    if(perm.equals(Permission.GOLD_TAKE_AMOUNT)){
                        lore.add("&cAmount: &e" + permValue + " Gold");
                    }else if(perm.equals(Permission.ITEM_TAKE_AMOUNT)){
                        lore.add("&cAmount: &e" + permValue + " Items");
                    }
                }

            }else{
               continue;
            }

            lore.add(" ");
            lore.add("&b" + permissionDescription);
            inv.setItem(index, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, permissionName, chatFactory.colorLore(lore)));
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
        if(clickedItem.getType().equals(backMat)){
            MembersMenu membersMenu = new MembersMenu(plugin, player);
            membersMenu.initializeItems(player);
            player.closeInventory();
            membersMenu.openInventory(player);
        }else if(clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)){

            byte data = clickedItem.getData().getData();
            /*
                ** 5 = true
                ** 14 = false
                ** 9 = amounts
                ** 2 = not set
             */
            if(data == (byte)5 || data == (byte)14 || data == (byte)9 || data == (byte)2){

                ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                String displayName = chatFactory.removeChatColor(clickedItem.getItemMeta().getDisplayName());
                displayName = displayName.replace(" ", "_");
                displayName = displayName.toUpperCase();
                Permission permission = Permission.valueOf(displayName);
                List<String> lore = clickedItemMeta.getLore();

                if(data == (byte)5){
                    clickedItem.setDurability((short)14);
                    lore.set(0, chatFactory.colorString("&cFalse"));
                    setPlayerPermission(permission, false);
                }else if(data == (byte)14){
                    clickedItem.setDurability((short)5);
                    lore.set(0, chatFactory.colorString("&aTrue"));
                    setPlayerPermission(permission, true);
                }else if(data == (byte)2){

                    clickedItem.setDurability((short)9);
                    if(permission == Permission.ITEM_TAKE_AMOUNT || permission == Permission.GOLD_TAKE_AMOUNT){
                        setPlayerPermission(permission, 0);
                    }

                    if(permission == Permission.GOLD_TAKE_AMOUNT){
                        lore.set(0, chatFactory.colorString("&cAmount: &e" + 0 + " Gold"));
                    }else if(permission == Permission.ITEM_TAKE_AMOUNT){
                        lore.set(0, chatFactory.colorString("&cAmount: &e" + 0 + " Items"));
                    }else{
                        clickedItem.setDurability((short)14);
                        lore.set(0, chatFactory.colorString("&cFalse"));
                        setPlayerPermission(permission, false);
                    }

                }else {
                    player.closeInventory();
                    ConversationFactory cf = new ConversationFactory(plugin);
                    Conversation conv = cf.withFirstPrompt(new NumericPermissionPrompt((DivisionsPlugin) plugin, permission, playerData)).withLocalEcho(false).buildConversation(player);
                    conv.begin();
                    return;
                }

                clickedItemMeta.setLore(lore);
                clickedItem.setItemMeta(clickedItemMeta);
                inv.setItem(slotNum, clickedItem);
            }
        }
    }

    private void setPlayerPermission(Permission perm, Object value){
        playerData.replacePermission(perm, value);
        playerDataManager.updatePlayerData(playerData.getPLAYER_UUID(), playerData);
    }

}
