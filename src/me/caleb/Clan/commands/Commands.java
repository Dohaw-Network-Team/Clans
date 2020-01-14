package me.caleb.Clan.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.caleb.Clan.Main;
import me.caleb.Clan.managers.ClanManager;
import me.caleb.Clan.utils.Utils;

public class Commands implements CommandExecutor{

	private Main plugin;
	
	public Commands(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("clan").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		if(!(sender instanceof Player)) {
			Utils.chat("You have to be a player to use this command!");
			return false;
		}else {
			
			ClanManager cm = new ClanManager(plugin);
			
			//clan create (clan name)
			if(args[0].equalsIgnoreCase("create") && args.length == 2) {
				cm.createClan(p.getName(), args[1]);
			}else if(args[0].equalsIgnoreCase("disband")) {
				cm.disbandClan(p.getName());
			}else if(args[0].equalsIgnoreCase("info")) {
				cm.showClanInfo(p.getName());
			}else if(args[0].equalsIgnoreCase("invite") && args.length == 2) {
				cm.invitePlayer(args[1], p.getName());
			}
			
		}
		
		
		
		return false;
	}

}
