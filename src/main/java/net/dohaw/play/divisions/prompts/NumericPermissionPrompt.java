package net.dohaw.play.divisions.prompts;

import me.c10coding.coreapi.chat.ChatFactory;
import me.c10coding.coreapi.helpers.EnumHelper;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class NumericPermissionPrompt extends NumericPrompt {

    private DivisionsPlugin plugin;
    private ChatFactory chatFactory;
    private DivisionsManager divisionsManager;
    private EnumHelper enumHelper;
    private Permission permission;
    private Division division;
    private Rank rank;

    private PlayerDataManager playerDataManager;
    private PlayerData playerData;

    /*
        For setting division permissions
     */
    public NumericPermissionPrompt(DivisionsPlugin plugin, Permission permission, Division division, Rank rank){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.permission = permission;
        this.divisionsManager = plugin.getDivisionsManager();
        this.enumHelper = plugin.getAPI().getEnumHelper();
        this.division = division;
        this.rank = rank;
    }

    /*
        For setting player permissions
     */
    public NumericPermissionPrompt(DivisionsPlugin plugin, Permission permission, PlayerData playerData){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.permission = permission;
        this.playerData = playerData;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.enumHelper = plugin.getAPI().getEnumHelper();
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

        int inputInt = input.intValue();
        if(division != null){
            division.setRankPermission(rank, permission, inputInt);
            divisionsManager.setDivision(division.getName(), division);
            context.getForWhom().sendRawMessage("Rank: " + enumHelper.enumToName(rank) + " | Permission: " + enumHelper.enumToName(permission) + " | New Value: " + inputInt);
        }else{
            playerData.replacePermission(permission, inputInt);
            playerDataManager.setPlayerData(playerData.getPlayerUUID(), playerData);
            context.getForWhom().sendRawMessage("Player: " + playerData.getPlayerName() + " | Permission: " + enumHelper.enumToName(permission) + " | New Value: " + inputInt);
        }

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
        return chatFactory.colorString("Please provide a new numerical value for this permission...");
    }
}
