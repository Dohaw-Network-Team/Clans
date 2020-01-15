package me.caleb.Clan.prompts;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.conversations.ValidatingPrompt;

import me.caleb.Clan.Main;
import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Clan.utils.Utils;

public class DisbandClanPrompt extends ValidatingPrompt{

	private String clanName, playerName;
	
	public DisbandClanPrompt(String clanName, String playerName) {
		this.clanName = clanName;
		this.playerName = playerName;
	}
	
	@Override
	public Prompt acceptValidatedInput(ConversationContext con, String input) {
		con.setSessionData("answer", input);
		con.setSessionData("clanName", clanName);
		return new DisbandDonePrompt();
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return Utils.sendMessageWithPrefix("Are you sure you wish to disband this clan? If you do, you lose all your clan gold, resources, and you lose your Garrison. Type &a&lYes&r if you wish to continue, or type &a&lNo&r if you want to cancel this action");
	}

	@Override
	protected boolean isInputValid(ConversationContext con, String input) {
		if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")) {
			return true;
		}else {
			return false;
		}
	}

}
