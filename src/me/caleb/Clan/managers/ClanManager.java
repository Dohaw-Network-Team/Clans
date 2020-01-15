package me.caleb.Clan.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.caleb.Clan.Main;
import me.caleb.Clan.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClanManager {

	private Main plugin;
	String clanName;
	
	public ClanManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public void createClan(String owner, String clanName, String publicOrPrivate) {
		ClanConfigManager.createClan(clanName, owner, publicOrPrivate);
	}

	public String getPlayerClan(String playerName) {
		return ClanConfigManager.getPlayerClan(playerName);
	}
	
	public void showClanInfo(String playerName) {
		ClanConfigManager.showClanInfo(playerName);
	}
	
	public void invitePlayer(String playerInvited, String inviter) {
		
		String invitedPlayerClan = ClanConfigManager.getPlayerClan(playerInvited);
		String ownerClan = ClanConfigManager.getPlayerClan(inviter);
		
		//If they do not have permission to invite and if they aren't the owner, then they can't invite
		if(!ClanConfigSettingManager.canInvite(ownerClan, inviter) && !ClanConfigManager.isOwner(ownerClan, inviter)) {
			Utils.sendPlayerMessage("You do not have the permission to invite players! Ask the owner to give you permission!", true, Bukkit.getPlayer(inviter));
			return;
		}
		
		OfflinePlayer pI = Bukkit.getOfflinePlayer(playerInvited);
		Player p = Bukkit.getPlayer(inviter);
		
		if(ClanConfigManager.isInClan(inviter)) {
			if(!pI.isOnline()) {
				Utils.sendPlayerMessage("This player is either not a valid player, or they aren't online right now!", true, p);
				return;
			}else {	
				if(ClanConfigManager.isInClan(playerInvited)) {
					if(invitedPlayerClan.equalsIgnoreCase(ownerClan)) {
						Utils.sendPlayerMessage("This player is already in your clan!", true, p);
						return;
					}else {
						Utils.sendPlayerMessage("This player is already in a clan!", true, p);
						return;
					}
				}else {
					
					Utils.sendPlayerMessage("You have invited " + pI.getName() + " to your clan!", true, p);
					
					TextComponent message = new TextComponent(Utils.chat("You have been invited to &a&o" + ownerClan + "&r by &a&o" + inviter + "&r. To accept this invite, "));
					TextComponent clickablePart = new TextComponent("click here!");
					clickablePart.setColor(ChatColor.GOLD);
					clickablePart.setBold(true);
					
					clickablePart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan invaccept " + ownerClan));
					clickablePart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Accept invite").create()));
					message.addExtra(clickablePart);
					Bukkit.getPlayer(playerInvited).spigot().sendMessage(message);
						
				}				
			}
		}else {
			Utils.sendPlayerMessage("You are not in a clan!", true, p);
			return;
		}
	}
	
	public void disbandClan(String playerName) {
		if(ClanConfigManager.isInClan(playerName)) {
			String clan = ClanConfigManager.getPlayerClan(playerName);
			if(ClanConfigManager.isOwner(clan, playerName)) {
				ClanConfigManager.disbandClan(playerName);
			}else {
				Utils.sendPlayerMessage("Only owners can disband the clan!", true, Bukkit.getPlayer(playerName));
				return;
			}
		}else {
			Player p = Bukkit.getPlayer(playerName);
			Utils.sendPlayerMessage("You are not apart of any clan!", true, p);
			return;
		}
		
		
		
	}

	public void acceptInvite(String clanName, String playerName) {
		try {
			if(ClanConfigManager.isInClan(playerName)) {
				if(ClanConfigManager.getPlayerClan(playerName).equalsIgnoreCase(clanName)) {
					Utils.sendPlayerMessage("You have already joined this clan!", true, Bukkit.getPlayer(playerName));
				}
				return;
			}else {
				ClanConfigManager.addMemberToClan(clanName, playerName);
				Utils.sendPlayerMessage("You have joined the clan &a&l" + clanName + "&r!", true, Bukkit.getPlayer(playerName));
				Player owner = Bukkit.getPlayer(ClanConfigManager.getClanOwner(clanName));
				Utils.sendPlayerMessage("&a&lThe player " + playerName + " has joined your clan!", true, owner);
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void kickMember(String playerName, String playerKicked) {
		
		Player p = Bukkit.getPlayer(playerName);
		OfflinePlayer pK = Bukkit.getOfflinePlayer(playerKicked);
		
		if(ClanConfigManager.isInClan(playerName)) {
			String clan = ClanConfigManager.getPlayerClan(playerName);
			if(!ClanConfigSettingManager.canKick(clan, playerName) && !ClanConfigManager.isOwner(clan, playerName)) {
				Utils.sendPlayerMessage("You do not have the permission to kick players from your clan!", true, p);
				return;
			}else {
				if(ClanConfigManager.isInClan(playerKicked)) {
					String playerKickedClan = ClanConfigManager.getPlayerClan(playerKicked);
					//If they are in the same clan
					if(!playerKickedClan.equalsIgnoreCase(clan)) {
						Utils.sendPlayerMessage("This player is not in your clan!", true, p);
					}else {
						ClanConfigManager.removeMemberFromClan(playerName, playerKicked, clan);
						Utils.sendPlayerMessage("You have kicked &a&l" + playerKicked + "&r from the clan!", true, p);
						if(pK.isOnline()) {
							Utils.sendPlayerMessage("You have been kicked from the clan &a&l" + clan + "&r by &a&l" + playerName, true, pK.getPlayer());
						}
					}
				}else {
					Utils.sendPlayerMessage("The player you are trying to kick is not in a clan!", true, p);
					return;
				}
			}
		}else {
			Utils.sendPlayerMessage("You are not in a clan!", true, p);
			return;
		}
	}

	public void leaveClan(String playerName) {
		
		Player p = Bukkit.getPlayer(playerName);
		
		if(!ClanConfigManager.isInClan(playerName)) {
			Utils.sendPlayerMessage("You aren't in a clan!", true, p);
			return;
		}else {
			String clanName = ClanConfigManager.getPlayerClan(playerName);
			if(ClanConfigManager.isOwner(clanName, playerName)) {
				Utils.sendPlayerMessage("You can\'t leave a clan when you\'re owner of it! You can either transfer ownership or disband the clan", true, p);
				return;
			}else {
				ClanConfigManager.removeMemberFromClan(playerName, clanName);
				Utils.sendPlayerMessage("You have left the clan &a&l" + clanName, true, p);
			}
		}
		
	}

	public void joinClan(String playerName, String clanName) {
		
		Player p = Bukkit.getPlayer(playerName);
		
		if(ClanConfigManager.isInClan(playerName)) {
			Utils.sendPlayerMessage("You are already in a clan!", true, p);
		}else {
			if(!ClanConfigManager.canJoinClan(clanName)) {
				Utils.sendPlayerMessage("This is a private clan! Ask the owner for an invite!", true, p);
			}else {
				Utils.sendPlayerMessage("You have joined the clan &a&l" + clanName, true, p);
				ClanConfigManager.addMemberToClan(clanName, playerName);
				OfflinePlayer owner = Bukkit.getOfflinePlayer(ClanConfigManager.getClanOwner(clanName));
				if(owner.isOnline()) {
					Utils.sendPlayerMessage("&a&l" + playerName + "&r has joined your clan!", true, owner.getPlayer());
				}
			}
		}
		
	}
	
}
