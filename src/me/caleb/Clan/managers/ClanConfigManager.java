package me.caleb.Clan.managers;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import me.caleb.Clan.Main;
import me.caleb.Clan.prompts.DisbandClanPrompt;
import me.caleb.Clan.utils.Utils;
import net.milkbowl.vault.economy.Economy;

public class ClanConfigManager {

	private static Main plugin = Main.getPlugin();
	private static String fileName = "clans.yml";
	private static File file = new File(plugin.getDataFolder(), "clans.yml");
	private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	public static List<String> clanList = config.getStringList("ClanList");
	private static Economy e = plugin.getEconomy();
	
	public static FileConfiguration getConfig() {
		return config;
	}
	
	public static void saveConfig() {
		try {
			config.save(file);
		}catch(IOException e) {
			plugin.getLogger().warning("Unable to save " + fileName);
		}
	}
	
	public static List<String> getList(String path){
		return config.getStringList(path);
	}
	
	public static String getValue(String path) {
		return config.getString(path);
	}
	
	public static void createClan(String clanName, String owner) {
		
		clanName = Utils.firstUppercaseRestLowercase(clanName);
		
		if(isClan(clanName)) {
			Utils.sendPlayerMessage("&rThis is already a clan!", true, Bukkit.getPlayer(owner));
			return;
		}else {
			if(isInClan(owner)) {
				Utils.sendPlayerMessage("&rYou are already in a clan!", true, Bukkit.getPlayer(owner));
				return;
			}else {
				Utils.sendPlayerMessage("&rThe clan &b&l" + clanName + " &rhas been created!", true, Bukkit.getPlayer(owner));
			}
		}
		
		ArrayList<String> members = new ArrayList<String>();
		members.add(owner);
		
		createClanBank(clanName, owner);
		
		clanList.add(clanName);
		config.set("ClanList", clanList);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		config.set("Clans." + clanName + ".Owner", owner);
		config.set("Clans." + clanName + ".Members", members);
		config.set("Clans." + clanName + ".GoldInBank", 0);
		config.set("Clans." + clanName + ".DateCreated", date);
		config.set("Clans." + clanName + ".Kills", 0);
		config.set("Clans." + clanName + ".Casualties", 0);
		config.set("Clans." + clanName + ".HeartsDestroyed", 0);
		config.set("Clans." + clanName + ".Power", 0);
		
		saveConfig();
		
	}
	
	public static boolean isClan(String clanName) {
		
		if(config.getString("Clans." + clanName) != null) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static boolean isInClan(String playerName) {
		
		if(clanList.size() == 0) return false;
		
		for(String clanName : clanList) {
			List<String> members = getMembers(clanName);
			if(members.contains(playerName)) {
				return true;
			}
		}
		
		return false;
	}

	public static List<String> getMembers(String clanName){
		return config.getStringList("Clans." + clanName + ".Members");
	}
	
	public static String getPlayerClan(String playerName) {
		
		for(String clanName : clanList) {
			List<String> members = getMembers(clanName);
			if(members.contains(playerName)) {
				return clanName;
			}
		}
		
		return null;
		
	}
	
	public static boolean isOwner(String clanName, String playerName) {
		if(config.getString("Clans." + clanName + ".Owner").equalsIgnoreCase(playerName)) {
			return true;
		}else {
			return false;
		}
	}
	
	public static void disbandClan(String owner) {
		
		if(isInClan(owner) == false) {
			Utils.sendPlayerMessage("You are not in a clan!", true, Bukkit.getPlayer(owner));
			return;
		}else {
			String clan = getPlayerClan(owner);
			if(isOwner(clan, owner)) {
				ConversationFactory factory = new ConversationFactory(plugin);
				Conversation conv = factory.withFirstPrompt(new DisbandClanPrompt(plugin, clan, owner)).withLocalEcho(false).withEscapeSequence("No").buildConversation(Bukkit.getPlayer(owner));
				conv.begin();
			}else {
				Utils.sendPlayerMessage("You cannot disand a clan that you are not owner of!", true, Bukkit.getPlayer(owner));
				return;
			}
		}
		
	}
	
	public static void removeClanFromConfig(String clanName) {
		
		int indexOfClan = clanList.indexOf(clanName);
		clanList.remove(indexOfClan);
		
		config.set("ClanList", clanList);
		config.set("Clans." + clanName + ".Owner", null);
		config.set("Clans." + clanName + ".Members", null);
		config.set("Clans." + clanName + ".GoldInBank", null);
		config.set("Clans." + clanName + ".DateCreated", null);
		config.set("Clans." + clanName + ".Kills", null);
		config.set("Clans." + clanName + ".Casualties", null);
		config.set("Clans." + clanName + ".HeartsDestroyed", null);
		config.set("Clans." + clanName + ".Power", null);
		config.set("Clans." + clanName, null);
	}
	
	public static void deleteClanBank(String clanName) {
		e.deleteBank(clanName + "Bank");
	}
	
	public static void createClanBank(String clanName, String owner) {
		e.createBank(clanName + "Bank", Bukkit.getOfflinePlayer(owner));
	}

	public static void showClanInfo(String playerName) {
		
		String clanName = getPlayerClan(playerName);
		Player p = Bukkit.getPlayer(playerName);
		
		Utils.sendPlayerMessage("Clan Info:", true, p);
		Utils.sendPlayerMessage("- &lName: &b&o" + clanName, false, p);
		Set<String> keys = config.getConfigurationSection("Clans." + clanName).getKeys(false);
		
		for(String key : keys) {
			String value = config.getString("Clans." + clanName + "." + key);
			Utils.sendPlayerMessage("- " + key + ": " + value, false, p);
		}
	}

	public static void invitePlayer(String inviter, String playerInvited) {
		if(isInClan(inviter)) {
			OfflinePlayer pI = Bukkit.getOfflinePlayer(playerInvited);
			Player p = Bukkit.getPlayer(inviter);
			if(!pI.isOnline()) {
				Utils.sendPlayerMessage("This player is either not a valid player, or they aren't online right now!", true, p);
			}
		}
	}
	
}
