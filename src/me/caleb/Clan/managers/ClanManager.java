package me.caleb.Clan.managers;

import java.util.List;

import me.caleb.Clan.Main;

public class ClanManager {

	private Main plugin;
	String clanName;
	
	public ClanManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public void createClan(String owner, String clanName) {
		ClanConfigManager.createClan(clanName, owner);
	}

	public String getPlayerClan(String playerName) {
		return ClanConfigManager.getPlayerClan(playerName);
	}
	
	public void showClanInfo(String playerName) {
		ClanConfigManager.showClanInfo(playerName);
	}
	
	public void invitePlayer(String playerInvited, String inviter) {
		ClanConfigManager.invitePlayer(inviter, playerInvited);
	}
	
	public void disbandClan(String playerName) {
		ClanConfigManager.disbandClan(playerName);
	}
}
