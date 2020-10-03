package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ConfirmableCommands implements CommandExecutor {

    private DivisionsPlugin plugin;
    private ChatFactory chatFactory;
    private DivisionsManager divisionsManager;
    private String prefix;

    /*
        A class for confirmable chat click events
     */
    public ConfirmableCommands(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.divisionsManager = plugin.getDivisionsManager();
        this.prefix = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            // Disband Division
            if(args[0].equalsIgnoreCase("disband") && args.length == 2){
                if(args[1].equalsIgnoreCase("abort")){
                    chatFactory.sendPlayerMessage("Aborting...", true, sender, prefix);
                }else{
                    String divisionName = args[1];
                    divisionsManager.disbandDivision(divisionName);
                    chatFactory.sendPlayerMessage("You have disbanded your division!", true, sender, prefix);
                }
            }else if(args[0].equalsIgnoreCase("invaccept") && args.length == 2){
                /*
                    They have been invited to a division
                 */
                if(plugin.hasBeenInvitedRecently(player.getUniqueId())){
                    if(!args[1].equalsIgnoreCase("abort")){

                        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
                        String divisionName = args[1];
                        Division division = divisionsManager.getDivision(divisionName);
                        PlayerData playerData = playerDataManager.getPlayerByUUID(player.getUniqueId());

                        playerData.setDivision(divisionName);
                        playerData.setRank(Rank.FRESH_MEAT);
                        playerDataManager.updatePlayerData(playerData);

                        division.addPlayer(playerData);
                        divisionsManager.updateDivision(divisionName, division);

                        for(UUID uuid : division.getPlayers()){
                            PlayerData member = playerDataManager.getPlayerByUUID(uuid);
                            if(member.getPlayer().isOnline()){
                                Player onlinePlayer = Bukkit.getPlayer(member.getPLAYER_UUID());
                                chatFactory.sendPlayerMessage("The player &e" + player.getName() + "&f has joined the division", true, onlinePlayer, prefix);
                            }
                        }

                    }else{
                        chatFactory.sendPlayerMessage("Aborting...", true, sender, prefix);
                    }
                    plugin.removeInvitedPlayer(player.getUniqueId());
                }else{
                    chatFactory.sendPlayerMessage("You haven't been invited to any divisions recently!", true, sender, prefix);
                }
            }
        }

        return false;
    }
}
