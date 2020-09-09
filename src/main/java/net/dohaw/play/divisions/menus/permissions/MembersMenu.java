package net.dohaw.play.divisions.menus.permissions;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.ItemStackHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
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
import java.util.UUID;

public class MembersMenu extends Menu implements Listener {

    private Player player;
    private ChatFactory chatFactory;

    /*
        Making previous menu null because it works right now without the previousMenu object
     */

    public MembersMenu(JavaPlugin plugin, Player player) {
        super((APIHook) plugin, null, "Member Permissions", 45);
        this.player = player;
        this.chatFactory = ((DivisionsPlugin)plugin).getAPI().getChatFactory();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        DivisionsManager divisionsManager = ((DivisionsPlugin)plugin).getDivisionsManager();
        PlayerDataManager playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        ItemStackHelper itemStackHelper = plugin.getAPI().getItemStackHelper();

        Division division = divisionsManager.getDivision(playerDataManager.getByPlayerObj(player).getDivision());

        int index = 0;
        for(UUID uuid : division.getPlayers()){
            PlayerData data = playerDataManager.getPlayerByUUID(uuid);
            if(data == null){
                data = playerDataManager.loadPlayerData(uuid);
            }
            ItemStack playerHead = getHead(data);
            //Their own head
            if(uuid.equals(player.getUniqueId())){
                playerHead = itemStackHelper.addGlowToItem(playerHead);
            }
            inv.setItem(index, playerHead);
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

        if(clickedItem.getType().equals(Material.LEGACY_SKULL_ITEM)){

            PlayerDataManager playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            UUID memberUUID = Bukkit.getOfflinePlayer(chatFactory.removeChatColor(meta.getDisplayName())).getUniqueId();

            PlayerData playerData = playerDataManager.getPlayerByUUID(memberUUID);
            if(playerData == null){
                playerData = playerDataManager.loadPlayerData(memberUUID);
            }

            MemberPermissionsMenu memberPermissionsMenu = new MemberPermissionsMenu(plugin, playerData);
            memberPermissionsMenu.initializeItems(player);
            player.closeInventory();
            memberPermissionsMenu.openInventory(player);

        }else if(clickedItem.getType().equals(backMat)){
            PermissionsMenu permissionsMenu = new PermissionsMenu(plugin);
            permissionsMenu.initializeItems(player);
            player.closeInventory();
            permissionsMenu.openInventory(player);
        }
    }

    private ItemStack getHead(PlayerData data) {

        ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1 , (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(data.getPLAYER_NAME());
        meta.setDisplayName(chatFactory.colorString("&e" + data.getPLAYER_NAME()));

        List<String> lore = new ArrayList<>();

        if(data.getRank() != null){
            lore.add(chatFactory.colorString("&cRank: &e" + data.getRank()));
        }else{
            lore.add(chatFactory.colorString("&cRank: &b&lLeader"));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }

}
