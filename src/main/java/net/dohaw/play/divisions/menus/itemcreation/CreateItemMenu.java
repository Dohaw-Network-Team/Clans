package net.dohaw.play.divisions.menus.itemcreation;

import lombok.Setter;
import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.customitems.ItemType;
import net.dohaw.play.divisions.customitems.Rarity;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.menus.itemcreation.enchants.DisplayEnchantsMenu;
import net.dohaw.play.divisions.menus.itemcreation.lore.DisplayLoreMenu;
import net.dohaw.play.divisions.prompts.itemcreation.ItemCreationSessionPrompt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CreateItemMenu extends Menu implements Listener {

    @Setter private ItemCreationSession session;
    private PlayerDataManager playerDataManager;

    public CreateItemMenu(APIHook plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu,"Create Item", 54);
        this.session = session;
        this.playerDataManager = ((DivisionsPlugin)plugin).getPlayerDataManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        Material sessionMat = session.getMaterial();
        String sessionDisplayName = session.getDisplayName();
        String sessionKey = session.getKey();
        ItemType sessionItemType = session.getItemType();
        Rarity sessionRarity = session.getRarity();
        List<String> sessionLore = session.getLore();

        /*
            Change Material
         */
        inv.setItem(10, createGuiItem(sessionMat, "&eChange Material", new ArrayList<>()));

        /*
            Change Display Name
         */
        List<String> displayNameLore = new ArrayList<String>(){{
            add("&cCurrent Display Name: &e" + sessionDisplayName);
        }};
        inv.setItem(12, createGuiItem(Material.PAPER, "&eChange Display Name", displayNameLore));

        /*
            Change Key
         */
        List<String> keyLore = new ArrayList<String>(){{
            add("&cCurrent Key: &e" + sessionKey);
        }};
        inv.setItem(14, createGuiItem(Material.TRIPWIRE_HOOK, "&eChange Key", keyLore));

        /*
            Change Item Type
         */
        List<String> itemTypeLore = new ArrayList<String>(){{
            add("&cCurrent Item Type: &e" + sessionItemType);
        }};
        Material itemTypeMat = sessionItemType.getMenuMat();
        inv.setItem(16, createGuiItem(itemTypeMat, "&eChange Item Type", itemTypeLore));

        /*
            Change Rarity
         */
        List<String> rarityLore = new ArrayList<String>(){{
            add("&cCurrent Rarity: &e" + sessionRarity.name());
        }};
        Material rarityMat = sessionRarity.getMenuMat();
        inv.setItem(28, createGuiItem(rarityMat, "&eChange Rarity", rarityLore));

        /*
            Change Lore
         */
        List<String> loreLore = new ArrayList<String>(){{
            add("&bClick me to edit the lore!");
        }};
        inv.setItem(30, createGuiItem(Material.WRITABLE_BOOK, "&eChange Lore", loreLore));

        List<String> enchantsLore = new ArrayList<String>(){{
            add("&bClick me to edit the enchants of this item!");
        }};
        inv.setItem(32, createGuiItem(Material.ENCHANTED_BOOK, "&eChange Enchants", enchantsLore));

        List<String> statsLore = new ArrayList<String>(){{
           add("&bClick me to edit the stats of this item!");
        }};
        inv.setItem(34, createGuiItem(Material.SLIME_BALL, "&eChange Stats", statsLore));

        boolean isSpellItem = session.isSpellItem();
        List<String> spellLore = new ArrayList<String>(){{
           add("&cIs Spell Item: &e" + isSpellItem);
        }};
        inv.setItem(40, createGuiItem(Material.BLAZE_POWDER, "&eChange If Spell Item", spellLore));

        //Abort button
        inv.setItem(inv.getSize() - 9, createGuiItem(Material.BARRIER, "&cAbort Creation", new ArrayList<>()));

        //Done button
        inv.setItem(inv.getSize() - 5, createGuiItem(Material.EMERALD_BLOCK, "&aCreate Item", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        setBackMaterial(Material.ARROW);
        fillMenu(true);
    }

    public void clearItems(){
        inv.clear();
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

        int abortCreationSlot = inv.getSize() - 9;
        int createItemSlot = inv.getSize() - 5;
        PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());

        if(slotClicked == 10 || slotClicked == 12 || slotClicked == 14){

            ItemCreationSessionPrompt.Change change;
            if(slotClicked == 10){
                change = ItemCreationSessionPrompt.Change.MATERIAL;
            }else if(slotClicked == 12){
                change = ItemCreationSessionPrompt.Change.DISPLAY_NAME;
            }else{
                change = ItemCreationSessionPrompt.Change.KEY;
            }

            DivisionsPlugin divPlugin = (DivisionsPlugin) plugin;

            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ItemCreationSessionPrompt(divPlugin.getCustomItemManager(), change, this, session, ((DivisionsPlugin)plugin).getPlayerDataManager() )).withLocalEcho(false).buildConversation(player);
            conv.begin();

            player.closeInventory();

        }else if(slotClicked == 16) {

            ItemType nextItemType = ItemType.getNextItemType(session.getItemType());
            session.setItemType(nextItemType);

            List<String> itemTypeLore = new ArrayList<String>() {{
                add("&cCurrent Item Type: " + nextItemType);
            }};
            inv.setItem(16, createGuiItem(nextItemType.getMenuMat(), "&eChange Item Type", itemTypeLore));

            /*
                Saves the session to the player data object.
             */
            pd.setItemCreationSession(session);

            playerDataManager.updatePlayerData(player.getUniqueId(), pd);

        }else if(slotClicked == 30) {
            DisplayLoreMenu dilm = new DisplayLoreMenu(plugin, this, session);
            dilm.initializeItems(player);
            player.closeInventory();
            dilm.openInventory(player);
        }else if(slotClicked == 28) {

            Rarity nextRarity = Rarity.getNextItemType(session.getRarity());
            session.setRarity(nextRarity);

            List<String> rarityLore = new ArrayList<String>() {{
                add("&cCurrent Rarity: &e" + nextRarity);
            }};
            inv.setItem(28, createGuiItem(nextRarity.getMenuMat(), "&eChange Rarity", rarityLore));

            /*
                Saves the session to the player data object.
             */
            pd.setItemCreationSession(session);

            playerDataManager.updatePlayerData(player.getUniqueId(), pd);

        }else if(slotClicked == 32) {

            DisplayEnchantsMenu dem = new DisplayEnchantsMenu(plugin, this, session);
            dem.initializeItems(player);
            player.closeInventory();
            dem.openInventory(player);

        }else if(slotClicked == 34) {

            DisplayStatsMenu dsm = new DisplayStatsMenu(plugin, this, session);
            dsm.initializeItems(player);
            player.closeInventory();
            dsm.openInventory(player);

        }else if(slotClicked == 40){

            boolean isSpellItem = session.isSpellItem();
            isSpellItem = !isSpellItem;

            boolean finalIsSpellItem = isSpellItem;
            List<String> spellLore = new ArrayList<String>(){{
                add("&cIs Spell Item: &e" + finalIsSpellItem);
            }};
            inv.setItem(40, createGuiItem(Material.BLAZE_POWDER, "&eChange If Spell Item", spellLore));

            session.setSpellItem(isSpellItem);

            pd.setItemCreationSession(session);
            playerDataManager.updatePlayerData(player.getUniqueId(), pd);

        }else if(slotClicked == abortCreationSlot){
            clearPlayerDataSession(pd);
            chatFactory.sendPlayerMessage("The item creation session has been aborted!", false, player, null);
            player.closeInventory();
        }else if(slotClicked == createItemSlot){

            if(!session.getKey().equalsIgnoreCase("none")){
                CustomItem cItem = session.toItem();
                CustomItemManager cim = ((DivisionsPlugin)plugin).getCustomItemManager();
                cim.addCustomItem(cItem);
            }else{
                chatFactory.sendPlayerMessage("Please change the key to something other than &e\"none\"!", false, player, null);
                return;
            }

            clearPlayerDataSession(pd);
            chatFactory.sendPlayerMessage("The item creation session has been created!", false, player, null);
            player.closeInventory();
        }else if(slotClicked == (inv.getSize() - 1)){
            goToPreviousMenu(player);
        }

    }


    private void clearPlayerDataSession(PlayerData pd){
        pd.setItemCreationSession(new ItemCreationSession());
        playerDataManager.updatePlayerData(pd.getPLAYER_UUID(), pd);
    }

}
