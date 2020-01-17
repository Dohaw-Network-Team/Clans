package me.caleb.Clan.managers;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import me.caleb.Clan.utils.Utils;

public class ClanConfigSettingManager extends ClanConfigManager{
	
	private static FileConfiguration config = getConfig();
	
	public static boolean canKick(String clanName, String playerName) {
		List<String> peopleThatCanKick = config.getStringList("ClansSettings." + clanName + ".Can Kick");
		
		if(peopleThatCanKick.contains(playerName)) {
			return true;
		}else {
			return false;
		}		
	}
	
	public static boolean canInvite(String clanName, String playerName) {
		List<String> peopleThatCanInv = config.getStringList("ClansSettings." + clanName + ".Can Invite");
		
		if(peopleThatCanInv.contains(playerName)) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String getRank(String clanName, String playerName) {
		
		if(isOwner(clanName,playerName)) return "Owner";
		
		Set<String> keys = config.getConfigurationSection("ClansSettings." + clanName + ".Roles").getKeys(false);
		
		for(String key : keys) {
			List<String> role = getList("ClansSettings." + clanName + ".Roles." + key);

			if(role.contains(playerName)) {
				return key;
			}
		}
		
		return "Newcomer";
		
		
	}
	
	public static void promoteOrDemotePlayer(String playerPromotingName, String playerCurrentRole, String playerName, String clanName, boolean shouldPromote) {
		
		final String[] ROLES = {"Newcomer", "Member", "Loyal", "Overlord"};
		int currentRoleIndex = 0;
		String newRole;
		
		for(int x = 0; x < ROLES.length; x++) {
			if(ROLES[x].equalsIgnoreCase(playerCurrentRole)) {
				currentRoleIndex = x;
			}
		}
		
		if(shouldPromote) {
			newRole = ROLES[currentRoleIndex+1];
		}else {
			newRole = ROLES[currentRoleIndex-1];
		}
		
		List<String> currentRoleList = getList("ClansSettings." + clanName + ".Roles." + playerCurrentRole);
		currentRoleList.remove(currentRoleList.indexOf(playerPromotingName));
		config.set("ClansSettings." + clanName + ".Roles." + playerCurrentRole, currentRoleList);
		
		List<String> newRoleList = getList("ClansSettings." + clanName + ".Roles." + newRole);
		newRoleList.add(playerPromotingName);
		config.set("ClansSettings." + clanName + ".Roles." + newRole, newRoleList);
		
		if(shouldPromote) {
			Utils.sendPlayerMessage("You have promoted &a&l" + playerPromotingName + "&r to &a&l" + newRole, true, Bukkit.getPlayer(playerName));
			Utils.sendPlayerMessage("You have been promoted to &a&l" + newRole + "&r by &a&l" + playerName, true, Bukkit.getPlayer(playerPromotingName));
		}else {
			Utils.sendPlayerMessage("You have demoted &a&l" + playerPromotingName + "&r to &a&l" + newRole, true, Bukkit.getPlayer(playerName));
			Utils.sendPlayerMessage("You have been demoted to &a&l" + newRole + "&r by &a&l" + playerName, true, Bukkit.getPlayer(playerPromotingName));
		}	
		
		saveConfig();
		
	}
	
	public static void setToNewcomer(String clanName, String playerName) {
		
		List<String> newcomers = getList("ClansSettings." + clanName + ".Newcomer");
		newcomers.add(playerName);
		config.set("ClansSettings." + clanName + ".Roles.Newcomer", newcomers);
		saveConfig();
	}
	
	public static boolean ifKickedHasHigherRank(String player1, String player2, String clanName) {
		
		final String[] ROLES = {"Newcomer", "Member", "Loyal", "Overlord"};
		
		if(isOwner(clanName, player1)) return false;
			
		String rank1 = getRank(clanName, player1);
		String rank2 = getRank(clanName, player2);
		
		int rank1Index = 0, rank2Index = 0;
		
		for(int x = 0;x < ROLES.length;x++) {
			if(rank1.equalsIgnoreCase(ROLES[x])) {
				rank1Index = x;
			}
			if(rank2.equalsIgnoreCase(ROLES[x])) {
				rank2Index = x;
			}
		}
		
		if(rank1Index > rank2Index) {
			return false;
		}else {
			return true;
		}
		
	}
	
	public static void addOrRemoveToPermissionList(String clanName, String actionAllowed, String allowedPlayer, String playerName, boolean added) {
		
		//People that can do whatever action specified in actionAllowed variable
		List<String> peopleThatCan;
		
		if(actionAllowed.equalsIgnoreCase("inv")) {
			peopleThatCan =  getList("ClansSettings." + clanName + ".Can Invite");
			actionAllowed = "Can Invite";
		}else {
			peopleThatCan = getList("ClansSettings." + clanName + ".Can Kick");
			actionAllowed = "Can Kick";
		}
		
		//If the player is in the list
		if(peopleThatCan.contains(allowedPlayer)) {
			//If the player is being allowed to do whatever action
			if(added == true) {
				Utils.sendPlayerMessage("This player is already allowed to do this action!", true, Bukkit.getPlayer(playerName));
				return;
			//If the player is not being allowed to do the action
			}else {
				OfflinePlayer op = Bukkit.getOfflinePlayer(allowedPlayer);
				peopleThatCan.remove(allowedPlayer);
				if(actionAllowed.equalsIgnoreCase("Can Invite")) {
					Utils.sendPlayerMessage("&a&l" + allowedPlayer + "&r cannot invite players to the clan anymore!", true, Bukkit.getPlayer(playerName));
					if(op.isOnline()) {
						Utils.sendPlayerMessage("You cannot invite players to the clan anymore!", true, Bukkit.getPlayer(allowedPlayer));
					}
				}else {
					Utils.sendPlayerMessage("&a&l" + allowedPlayer + "&r cannot kick players from the clan! anymore!", true, Bukkit.getPlayer(playerName));
					if(op.isOnline()) {
						Utils.sendPlayerMessage("You cannot kick players from the clan anymore!", true, Bukkit.getPlayer(allowedPlayer));
					}	
				}
			}
		}else {
			if(added == true) {
				OfflinePlayer op = Bukkit.getOfflinePlayer(allowedPlayer);
				peopleThatCan.add(allowedPlayer);
				if(actionAllowed.equalsIgnoreCase("Can Invite")) {
					Utils.sendPlayerMessage("&a&l" + allowedPlayer + "&r can now invite players to the clan!", true, Bukkit.getPlayer(playerName));
					if(op.isOnline()) {
						Utils.sendPlayerMessage("You have been given the permission to invite players!", true, Bukkit.getPlayer(allowedPlayer));
					}
				}else {
					Utils.sendPlayerMessage("&a&l" + allowedPlayer + "&r can now kick players from the clan!", true, Bukkit.getPlayer(playerName));
					if(op.isOnline()) {
						Utils.sendPlayerMessage("You can kick players from the clan now!", true, Bukkit.getPlayer(allowedPlayer));
					}
				}
			}else {
				Utils.sendPlayerMessage("This player already can\'t do that action!", true, Bukkit.getPlayer(playerName));
				return;
			}	
		}
		
		config.set("ClansSettings." + clanName + "." + actionAllowed, peopleThatCan);
		saveConfig();
		
	}
	
}
