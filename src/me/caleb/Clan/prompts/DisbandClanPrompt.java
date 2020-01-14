package me.caleb.Clan.prompts;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import me.caleb.Clan.Main;
import me.caleb.Clan.managers.ClanConfigManager;
import me.caleb.Clan.utils.Utils;

public class DisbandClanPrompt extends StringPrompt{

	private Main plugin;
	private String clanName, playerName;
	
	public DisbandClanPrompt(Main plugin, String clanName, String playerName) {
		this.plugin = plugin;
		this.clanName = clanName;
		this.playerName = playerName;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {

		if(!answer.equalsIgnoreCase("Yes")) return null;
		
		//Garrison.delete();
		ClanConfigManager.deleteClanBank(clanName);
		ClanConfigManager.removeClanFromConfig(clanName);
		ClanConfigManager.saveConfig();
		Utils.sendPlayerMessage("You have disbanded your clan!", true, Bukkit.getPlayer(playerName));
		return null;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return Utils.sendMessageWithPrefix("Are you sure you wish to disband this clan? If you do, you lose all your clan gold, resources, and you lose your Garrison. Type Yes if you wish to continue, or type No if you want to cancel this action");
	}

}
