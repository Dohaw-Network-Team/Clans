package me.caleb.Clan.managers;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

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
	
}
