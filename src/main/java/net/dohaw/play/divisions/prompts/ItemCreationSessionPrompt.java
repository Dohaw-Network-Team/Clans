package net.dohaw.play.divisions.prompts;

import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.menus.itemcreation.CreateItemMenu;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemCreationSessionPrompt extends StringPrompt {

    private CustomItemManager customItemManager;
    private ItemCreationSession session;
    private PlayerDataManager playerDataManager;
    private CreateItemMenu previousMenu;
    private Change change;

    private int indexLoreLine;

    /*
        For editing everything else
     */
    public ItemCreationSessionPrompt(CustomItemManager customItemManager, Change change, CreateItemMenu previousMenu, ItemCreationSession session, PlayerDataManager playerDataManager){
        this.session = session;
        this.playerDataManager = playerDataManager;
        this.previousMenu = previousMenu;
        this.change = change;
        this.customItemManager = customItemManager;
    }

    /*
        For editing a lore line
     */
    public ItemCreationSessionPrompt(Change change, CreateItemMenu previousMenu, ItemCreationSession session, int indexLoreLine, PlayerDataManager playerDataManager){
        this.session = session;
        this.playerDataManager = playerDataManager;
        this.previousMenu = previousMenu;
        this.change = change;
        this.indexLoreLine = indexLoreLine;
    }

    /*
        What can be changed within a Session
     */
    public enum Change{
        MATERIAL,
        DISPLAY_NAME,
        KEY,
        EDIT_LORE_LINE,
        ADD_LORE_LINE,
    }

    /**
     * Gets the text to display to the user when this prompt is first
     * presented.
     *
     * @param context Context information about the conversation.
     * @return The text to display.
     */
    @Override
    public String getPromptText(ConversationContext context) {
        if(change == Change.MATERIAL){
            return "Please type what you would like the material for this item to be. This is NOT case-sensitive...";
        }else if(change == Change.DISPLAY_NAME) {
            return "Please type what you would like the display name for this item to be...";
        }else if(change == Change.EDIT_LORE_LINE || change == Change.ADD_LORE_LINE) {
            return "Please type what you would like the new lore line to be...";
        }else{
            return "Please type what you would like the key for this item to be...";
        }
    }

    /**
     * Accepts and processes input from the user. Using the input, the next
     * Prompt in the prompt graph is returned.
     *
     * @param context Context information about the conversation.
     * @param input   The input text from the user.
     * @return The next Prompt in the prompt graph.
     */
    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        Player player = (Player) context.getForWhom();

        if(change == Change.MATERIAL){

            Material newMat;
            try{
                newMat = Material.valueOf(input.toUpperCase());
                player.sendRawMessage("The material for this custom item has been set to " + newMat.name() + "!");
            }catch(IllegalArgumentException e){
                player.sendRawMessage("The material you desire can't be found! The material has been defaulted to an apple!");
                newMat = Material.APPLE;
            }
            session.setMaterial(newMat);

        }else if(change == Change.DISPLAY_NAME) {
            session.setDisplayName(input);
            player.sendRawMessage("The display name for this custom item has been set to " + input);
        }else if(change == Change.EDIT_LORE_LINE || change == Change.ADD_LORE_LINE){

            List<String> lore = session.getLore();
            if(change == Change.EDIT_LORE_LINE){
                lore.set(indexLoreLine, input);
            }else{
                lore.add(input);
            }

            session.setLore(lore);
            player.sendRawMessage("The lore for this custom item has been updated!");
        }else{
            if(!customItemManager.hasExistingKey(input)){
                session.setKey(input.toLowerCase());
                player.sendRawMessage("The key for this custom item has been set to " + input.toLowerCase());
            }else{
                player.sendRawMessage("This is already a key! Please choose another one...");
                goToPreviousMenu(player);
                return END_OF_CONVERSATION;
            }
        }

        PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
        pd.setItemCreationSession(session);
        playerDataManager.updatePlayerData(player.getUniqueId(), pd);

        previousMenu.setSession(session);

        previousMenu.clearItems();
        goToPreviousMenu(player);

        return END_OF_CONVERSATION;
    }

    private void goToPreviousMenu(Player player){
        previousMenu.initializeItems(player);
        previousMenu.openInventory(player);
    }
}
