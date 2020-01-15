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
			if(args[0].equalsIgnoreCase("create") && args.length == 3) {
				cm.createClan(p.getName(), args[1], args[2]);
			}else if(args[0].equalsIgnoreCase("disband")) {
				cm.disbandClan(p.getName());
			}else if(args[0].equalsIgnoreCase("info")) {
				if(args.length == 2) {
					cm.showClanInfo(p.getName(), args[1]);
				}else {
					cm.showClanInfo(p.getName());
				}
				
			}else if(args[0].equalsIgnoreCase("inv") && args.length == 2) {
				cm.invitePlayer(args[1], p.getName());
			}else if(args[0].equalsIgnoreCase("invaccept") && args.length == 2) {
				cm.acceptInvite(args[1], p.getName());
			}else if(args[0].equalsIgnoreCase("kick") && args.length == 2) {
				cm.kickMember(p.getName(), args[1]);
			}else if(args[0].equalsIgnoreCase("leave")) {
				cm.leaveClan(p.getName());
			}else if(args[0].equalsIgnoreCase("join") && args.length == 2) {
				cm.joinClan(p.getName(),args[1]);
			}else if(args[0].equalsIgnoreCase("set") && args.length == 2) {
				cm.setAvailability(p.getName(), args[1]);
			//clan allow (Player) (Invite | Kick)
			}else if(args[0].equalsIgnoreCase("allow") && args.length == 3) {
				//cm.allowPlayer(p.getName(), args[1], args[2]);
			}else if(args[0].equalsIgnoreCase("promote") && args.length == 2){
				cm.promoteOrDemotePlayer(p.getName(), args[1], true);
			}else if(args[0].equalsIgnoreCase("demote") && args.length == 2) {
				cm.promoteOrDemotePlayer(p.getName(), args[1], false);
			}
			
		}
		
		
		
		return false;
	}

}
