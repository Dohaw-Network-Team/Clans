package me.caleb.Clan.prompts;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Clan.utils.Utils;

public class DisbandDonePrompt implements Prompt{

	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg1) {
		return END_OF_CONVERSATION;
	}

	@Override
	public boolean blocksForInput(ConversationContext arg0) {
		return false;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Conversable c = context.getForWhom();
		
		String answer = (String) context.getSessionData("answer");
		String clanName = (String) context.getSessionData("clanName");
		
		if(answer.equalsIgnoreCase("Yes")) {
			ClanConfigManager.deleteClanBank(clanName);
			ClanConfigManager.removeClanFromConfig(clanName);
			ClanConfigManager.saveConfig();
			return Utils.sendMessageWithPrefix("Your clan has been disbanded!");
		}else {
			return Utils.sendMessageWithPrefix("Your clan has not been disbanded!");
		}
		
	}

}
