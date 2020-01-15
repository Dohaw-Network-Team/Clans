package me.caleb.Clan.managers;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.caleb.Clan.utils.Utils;

public class ClanConfigSettingManager extends ClanConfigManager{
	
	private static FileConfiguration config = getConfig();
	
	public static boolean canKick(String clanName, String playerName) {
		List<String> peopleThatCanKick = config.getStringList("ClansSettings." + clanName + ".CanKick");
		
		if(peopleThatCanKick.contains(playerName)) {
			return true;
		}else {
			return false;
		}		
	}
	
	public static boolean canInvite(String clanName, String playerName) {
		List<String> peopleThatCanInv = config.getStringList("ClansSettings." + clanName + ".CanInvite");
		
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
	
}
