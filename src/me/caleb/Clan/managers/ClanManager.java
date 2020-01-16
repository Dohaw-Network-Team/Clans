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

public class ClanManager{

	private Main plugin;
	
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
	
	public void showClanInfo(String playerName, String clanName) {
		ClanConfigManager.showClanInfo(playerName, clanName);
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
				ClanConfigSettingManager.setToNewcomer(clanName, playerName);
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
		
		if(ClanConfigManager.isClan(clanName)) {
			if(ClanConfigManager.isInClan(playerName)) {
				Utils.sendPlayerMessage("You are already in a clan!", true, p);
			}else {
				if(!ClanConfigManager.canJoinClan(clanName)) {
					Utils.sendPlayerMessage("This is a private clan! Ask the owner for an invite!", true, p);
				}else {
					Utils.sendPlayerMessage("You have joined the clan &a&l" + clanName, true, p);
					ClanConfigManager.addMemberToClan(clanName, playerName);
					OfflinePlayer owner = Bukkit.getOfflinePlayer(ClanConfigManager.getClanOwner(clanName));
					ClanConfigSettingManager.setToNewcomer(clanName, playerName);
					if(owner.isOnline()) {
						Utils.sendPlayerMessage("&a&lThe player " + playerName + " has joined your clan!", true, owner.getPlayer());
					}
				}
			}
		}else {
			Utils.sendPlayerMessage("This is not a clan!", true, p);
			return;
		}	
	}

	public void setAvailability(String playerName, String av) {
		
		Player p = Bukkit.getPlayer(playerName);
		
		if(!ClanConfigManager.isInClan(playerName)) {
			Utils.sendPlayerMessage("You are not in a clan!", true, p);
			return;
		}else {
			
			String clanName = ClanConfigManager.getPlayerClan(playerName);
			
			if(!ClanConfigManager.isOwner(clanName, playerName)) {
				Utils.sendPlayerMessage("You can only use this command as owner!", true, p);
				return;
			}else {
				if(!av.equalsIgnoreCase("public") && !av.equalsIgnoreCase("private")) {
					Utils.sendPlayerMessage("To set change the clan availability, use the command &a/clan set <public | private>", true, p);
				}else {
					if(ClanConfigManager.getAvailability(clanName).equalsIgnoreCase(av)) {
						Utils.sendPlayerMessage("Your clan is already set to &a&l" + av.toLowerCase(), true, p);
						return;
					}else {
						ClanConfigManager.alterClanAvailability(av, clanName);
						if(av.equalsIgnoreCase("public")) {
							Utils.sendPlayerMessage("You have set the clan\'s availability to &a&l" + av + ".&r This means that players will be able to join your clan without an invite!", true, p);
						}else {
							Utils.sendPlayerMessage("You have set the clan\'s availability to &a&l" + av + ".&r This means that players will only be able to join your clan if you invite them!", true, p);
						}	
					}	
				}
			}	
		}
	}
	
	public void allowOrRefusePlayer(String playerName, String action, String allowedPlayer, String a) {
		
		Player p = Bukkit.getPlayer(playerName);
		String clanName = ClanConfigManager.getPlayerClan(playerName);
		String rank = ClanConfigSettingManager.getRank(clanName, playerName);
		
		boolean added = Boolean.parseBoolean(a);
		
		if(ClanConfigManager.isInClan(playerName) && ClanConfigManager.isInClan(playerName)) {
			if(!rank.equalsIgnoreCase("Overlord") && !ClanConfigManager.isOwner(clanName, playerName)) {
				Utils.sendPlayerMessage("Only the Overlord and Owner can use this command!", true, p);
				return;	
			}else {
				
				if(playerName.equalsIgnoreCase(allowedPlayer)) {
					Utils.sendPlayerMessage("You cannot use this command on yourself!", true, p);
				}else {
					if(!action.equalsIgnoreCase("inv") && !action.equalsIgnoreCase("kick")) {
						Utils.sendPlayerMessage("The action was not recognized. The command is &a/clan allow <player name> <inv | kick> <true | false>", true, p);
						return;
					}else {
						if(!ClanConfigManager.inSameClan(playerName, allowedPlayer)) {
							Utils.sendPlayerMessage("This player is not in your clan!", true, p);
							return;
						}else {
							if(added) {
								if(ClanConfigManager.isOwner(clanName, allowedPlayer)) {
									Utils.sendPlayerMessage("You cannot use this command on an owner!", true, p);
								}else {
									ClanConfigSettingManager.addOrRemoveToPermissionList(clanName, action, allowedPlayer, playerName, true);
								}
							}else {
								if(ClanConfigManager.isOwner(clanName, allowedPlayer)) {
									Utils.sendPlayerMessage("You cannot use this command on an owner!", true, p);
								}else {
									ClanConfigSettingManager.addOrRemoveToPermissionList(clanName, action, allowedPlayer, playerName, false);
								}
							}	
						}
					}
				}	
			}
		}else {
			Utils.sendPlayerMessage("You or the player you are trying to give permissions to is not in a clan!", true, p);
			return;
		}
		
		
	}

	public void promoteOrDemotePlayer(String playerName, String playerPromotingName, boolean shouldPromote) {
		
		if(playerName.equalsIgnoreCase(playerPromotingName)) {
			if(shouldPromote) {
				Utils.sendPlayerMessage("You can\'t promote yourself!", true, Bukkit.getPlayer(playerName));
			}else {
				Utils.sendPlayerMessage("You can\'t demote yourself!", true, Bukkit.getPlayer(playerName));
			}
			return;
		}
		
		if(ClanConfigManager.isInClan(playerName) && ClanConfigManager.isInClan(playerPromotingName)) {
			
			String playerClanName = ClanConfigManager.getPlayerClan(playerName);
			String playerPromotingClanName = ClanConfigManager.getPlayerClan(playerPromotingName);
			String ppCurrentRole = ClanConfigSettingManager.getRank(playerClanName, playerPromotingName);
			String playerCurrentRole;
			playerCurrentRole = ClanConfigSettingManager.getRank(playerClanName, playerName);
			
			if(!playerClanName.equalsIgnoreCase(playerPromotingClanName)) {
				Utils.sendPlayerMessage("This player is not in your clan!", true, Bukkit.getPlayer(playerName));
				return;
			}else {
				if(!ClanConfigManager.isOwner(playerClanName, playerName)) {	
					if(!playerCurrentRole.equalsIgnoreCase("Overlord")) {
						Utils.sendPlayerMessage("You aren\'t allowed to promote players at your current clan rank!", true, Bukkit.getPlayer(playerName));
						return;
					}else {
						Player p = Bukkit.getPlayer(playerName);
						
						final String[] ROLES = {"Newcomer", "Member", "Loyal", "Overlord"};
						
						if(shouldPromote) {
							if(ppCurrentRole.equalsIgnoreCase("Overlord") || ClanConfigManager.isOwner(playerClanName, playerPromotingName)) {
								Utils.sendPlayerMessage("This player is already at the highest rank!", true, p);
								return;
							}else if(ClanConfigManager.isOwner(playerClanName, playerPromotingName) || ppCurrentRole.equalsIgnoreCase("Overlord")){
								Utils.sendPlayerMessage("You can\'t promote someone who is the same rank or higher than you!", true, p);
								return;
							}else {
								ClanConfigSettingManager.promoteOrDemotePlayer(playerPromotingName, ppCurrentRole, playerName, playerClanName, shouldPromote);
							}
						}else {
							if(ppCurrentRole.equalsIgnoreCase("Newcomer")) {
								Utils.sendPlayerMessage("This player is already at the lowest rank!", true, p);
								return;
							}else if(ClanConfigManager.isOwner(playerClanName, playerPromotingName) || ppCurrentRole.equalsIgnoreCase("Overlord")) {
								Utils.sendPlayerMessage("You can\'t demote someone who is the same rank or higher than you!", true, p);
								return;
							}else {
								ClanConfigSettingManager.promoteOrDemotePlayer(playerPromotingName, ppCurrentRole, playerName, playerClanName, shouldPromote);
							}
						}
							
					}
				}else {
					if(shouldPromote) {
						if(ppCurrentRole.equalsIgnoreCase("Overlord")) {
							Utils.sendPlayerMessage("This player is already at the highest rank!", true, Bukkit.getPlayer(playerName));
							return;
						}else {
							ClanConfigSettingManager.promoteOrDemotePlayer(playerPromotingName, ppCurrentRole, playerName, playerClanName, shouldPromote);
						}
					}else {
						if(ppCurrentRole.equalsIgnoreCase("Newcomer")) {
							Utils.sendPlayerMessage("This player is already at the lowest rank!", true, Bukkit.getPlayer(playerName));
							return;
						}else {
							ClanConfigSettingManager.promoteOrDemotePlayer(playerPromotingName, ppCurrentRole, playerName, playerClanName, shouldPromote);
						}
					}
				}	
				
			}
				
		}else {
			Utils.sendPlayerMessage("You or the player that you are trying to promote is not in a clan!", true, Bukkit.getPlayer(playerName));
			return;
		}
	}

	
	
}
