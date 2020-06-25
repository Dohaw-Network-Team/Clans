package net.dohaw.play.divisions.menus;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.ItemStackHelper;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
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

    public MembersMenu(JavaPlugin plugin, Player player) {
        super(plugin, "Member Permissions", 45);
        this.player = player;
        this.chatFactory = ((DivisionsPlugin)plugin).getCoreAPI().getChatFactory();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        DivisionsManager divisionsManager = ((DivisionsPlugin)plugin).getDivisionsManager();
        PlayerDataManager playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        ItemStackHelper itemStackHelper = ((DivisionsPlugin)plugin).getCoreAPI().getItemStackHelper();

        Division division = divisionsManager.getDivision(playerDataManager.getByPlayerObj(player).getDivision());

        int index = 0;
        for(PlayerData data : division.getPlayers()){
            ItemStack playerHead = getHead(data);
            //Their own head
            if(data.getPlayerUUID().equals(player.getUniqueId())){
                playerHead = itemStackHelper.addGlowToItem(playerHead);
            }
            inv.setItem(index, playerHead);
            index++;
        }

        fillMenu(true);

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(fillerMat)) return;
        if(!(e.getWhoClicked() instanceof Player)) return;

        if(clickedItem.getType().equals(Material.SKULL_ITEM)){
            PlayerDataManager playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            UUID memberUUID = Bukkit.getPlayer(chatFactory.removeChatColor(meta.getDisplayName())).getUniqueId();
            PlayerData playerData = playerDataManager.getPlayerByUUID(memberUUID);
            if(playerData != null){
                MemberPermissionsMenu memberPermissionsMenu = new MemberPermissionsMenu(plugin, playerData);
                memberPermissionsMenu.initializeItems(player);
                player.closeInventory();
                memberPermissionsMenu.openInventory(player);
            }
        }

    }

    private ItemStack getHead(PlayerData data) {

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1 , (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(data.getPlayerName());
        meta.setDisplayName(chatFactory.colorString("&e" + data.getPlayerName()));

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
