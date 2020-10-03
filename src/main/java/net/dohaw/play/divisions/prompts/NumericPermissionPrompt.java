package net.dohaw.play.divisions.prompts;

import net.dohaw.play.corelib.StringUtils;
import net.dohaw.play.corelib.helpers.EnumHelper;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class NumericPermissionPrompt extends NumericPrompt {

    private DivisionsManager divisionsManager;
    private Permission permission;
    private Division division;
    private Rank rank;

    private PlayerDataManager playerDataManager;
    private PlayerData playerData;

    /*
        For setting division permissions
     */
    public NumericPermissionPrompt(DivisionsPlugin plugin, Permission permission, Division division, Rank rank){
        this.permission = permission;
        this.divisionsManager = plugin.getDivisionsManager();
        this.division = division;
        this.rank = rank;
    }

    /*
        For setting player permissions
     */
    public NumericPermissionPrompt(DivisionsPlugin plugin, Permission permission, PlayerData playerData){
        this.playerDataManager = plugin.getPlayerDataManager();
        this.permission = permission;
        this.playerData = playerData;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {

        int inputInt = input.intValue();
        if(division != null){
            division.setRankPermission(rank, permission, inputInt);
            divisionsManager.updateDivision(division.getName(), division);
            context.getForWhom().sendRawMessage("Rank: " + EnumHelper.enumToName(rank) + " | Permission: " + EnumHelper.enumToName(permission) + " | New Value: " + inputInt);
        }else{
            playerData.replacePermission(permission, inputInt);
            playerDataManager.updatePlayerData(playerData);
            context.getForWhom().sendRawMessage("Player: " + playerData.getPLAYER_NAME() + " | Permission: " + EnumHelper.enumToName(permission) + " | New Value: " + inputInt);
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
        return StringUtils.colorString("Please provide a new numerical value for this permission...");
    }
}
