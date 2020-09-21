package net.dohaw.play.divisions.prompts.itemcreation;

import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.customitems.ItemCreationSession;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.menus.itemcreation.CreateItemMenu;
import net.dohaw.play.divisions.utils.MenuHelper;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Map;

public class EnchantmentLevelPrompt extends NumericPrompt {

    private Enchantment enchantment;
    private CreateItemMenu previousMenu;
    private ItemCreationSession session;
    private PlayerDataManager playerDataManager;

    public EnchantmentLevelPrompt(Enchantment enchantment, CreateItemMenu previousMenu, ItemCreationSession session, PlayerDataManager playerDataManager){
        this.enchantment = enchantment;
        this.previousMenu = previousMenu;
        this.session = session;
        this.playerDataManager = playerDataManager;
    }

    /**
     * Override this method to perform some action with the user's integer
     * response.
     *
     * @param context Context information about the conversation.
     * @param input   The user's response as a {@link Number}.
     * @return The next {@link Prompt} in the prompt graph.
     */
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {

        Player player = (Player) context.getForWhom();
        Map<Enchantment, Integer> sessionEnchants = session.getEnchants();
        sessionEnchants.put(enchantment, input.intValue());
        session.setEnchants(sessionEnchants);

        PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());
        pd.setItemCreationSession(session);
        playerDataManager.updatePlayerData(player.getUniqueId(), pd);

        previousMenu.setSession(session);

        previousMenu.clearItems();
        MenuHelper.goToPreviousMenu(previousMenu, player);

        return END_OF_CONVERSATION;
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
        return "Please enter what you would like the level of the enchantment to be!";
    }
}
