package me.caleb.Clan.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

	final static String prefix = "&8&l[&aClans&8&l]";
	
	public static String firstUppercaseRestLowercase(String s) {
		return s.substring(0,1) + s.substring(1);
	}
	
	public static String valueOf(int x) {
		return String.valueOf(x);
	}
	
	public static String valueOf(double x) {
		return String.valueOf(x);
	}

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static void sendConsoleMessage(String s) {
		Bukkit.getConsoleSender().sendMessage(chat(prefix + " &r" + s));
	}
	
	public static void sendPlayerMessage(String s, boolean wantPrefix, Player p) {
		if(wantPrefix) {
			p.sendMessage(chat(prefix + " &r" + s));
		}else {
			p.sendMessage(chat("&r" + s));
		}
	}
	
	public static String sendMessageWithPrefix(String s) {
		return chat(prefix + " &r" + s);
	}
	
}
