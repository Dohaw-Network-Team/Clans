package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.division.DivisionStatus;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.menus.PermissionsMenu;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;
import net.dohaw.play.divisions.utils.PlayerHelper;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DivisionsCommand implements CommandExecutor {

    private DivisionsPlugin plugin;
    private DivisionsManager divisionsManager;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private String prefix;

    public DivisionsCommand(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.divisionsManager = plugin.getDivisionsManager();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.chatFactory = plugin.getCoreAPI().getChatFactory();
        this.prefix = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length > 0){
                //divisions create <NAME> <PUBLIC | PRIVATE>
                if(args[0].equalsIgnoreCase("create") && args.length == 3){
                    String divisionName = args[1];
                    String status = args[2];
                    if(playerDataManager.getByPlayerObj(player).getDivision() == null){
                        if(!divisionsManager.hasContent(divisionName)){
                            if(status.equalsIgnoreCase("public") || status.equalsIgnoreCase("private")){
                                /*
                                    Prevents a player from triggering a confirmable command
                                 */
                                if(!divisionName.equalsIgnoreCase("abort")){

                                    /*
                                        public = anyone can join
                                        private = need an invite
                                     */
                                    if(status.equalsIgnoreCase("public")){
                                        divisionsManager.createNewDivision(divisionName, player, DivisionStatus.PUBLIC);
                                    }else{
                                        divisionsManager.createNewDivision(divisionName, player, DivisionStatus.PRIVATE);
                                    }

                                    playerDataManager.getByPlayerObj(player).setPlayerDivision(divisionsManager.getDivision(divisionName).getName());
                                    playerDataManager.getByPlayerObj(player).setRank(null);
                                    chatFactory.sendPlayerMessage("Created a new division called &a&l" + divisionName + "!", true, player, prefix);
                                }else{
                                    chatFactory.sendPlayerMessage("You can't name your division this!", true, player, prefix);
                                }
                            }else{
                                chatFactory.sendPlayerMessage("Your division can only be a public or private division.", true, player, prefix);
                            }
                        }else{
                            chatFactory.sendPlayerMessage("This is already a division!", true, player, prefix);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You are already in a division!", true, player, prefix);
                    }
                }else if(args[0].equalsIgnoreCase("disband") && args.length == 1){
                    String divisionName = playerDataManager.getPlayerByUUID(player.getUniqueId()).getDivision();
                    Division division = divisionsManager.getDivision(divisionName);
                    if(playerDataManager.getPlayerByUUID(player.getUniqueId()).getDivision() != null){
                        if(division.getLeader().getPlayerUUID().equals(player.getUniqueId())) {

                            TextComponent msg = new TextComponent(chatFactory.colorString(prefix) + " Are you sure you wish to disband your division? By doing so, you will keep your Garrison, but lose all Division stats, power, and more! Press ");
                            TextComponent yes = new TextComponent(chatFactory.colorString("&a&lYes"));
                            TextComponent afterYes = new TextComponent(chatFactory.colorString("&f to continue with this actions or "));
                            TextComponent no = new TextComponent(chatFactory.colorString("&4&lNo"));
                            TextComponent afterNo = new TextComponent(chatFactory.colorString("&f to not disband your division"));

                            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/divisionsconfirm disband " + divisionName));
                            yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Disband Division").create()));

                            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/divisionsconfirm disband abort"));
                            no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Abort...").create()));

                            msg.addExtra(yes);
                            msg.addExtra(afterYes);
                            msg.addExtra(no);
                            msg.addExtra(afterNo);

                            player.spigot().sendMessage(msg);

                        }else{
                            chatFactory.sendPlayerMessage("Only the leader can perform this command!", true, player, prefix);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You aren't in a division!", true, player, prefix);
                    }
                }else if(args[0].equalsIgnoreCase("info")){
                    if(playerDataManager.getByPlayerObj(player).getDivision() != null){
                        if(args.length == 2){
                            String divisionArg = args[1];
                            if(divisionsManager.getDivision(divisionArg) != null){
                                String divisionName = playerDataManager.getByPlayerObj(player).getDivision();
                                Division division = divisionsManager.getDivision(divisionName);
                                displayDivisionInfo(division, player);
                            }
                        }else{
                            String divisionName = playerDataManager.getByPlayerObj(player).getDivision();
                            Division playerDivision = divisionsManager.getDivision(divisionName);
                            displayDivisionInfo(playerDivision, player);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You aren't in a division right now!", true, player, prefix);
                    }
                }else if(args[0].equalsIgnoreCase("invite") && args.length == 2){
                    String playerName = args[1];
                    if(playerDataManager.getByPlayerObj(player).getDivision() != null){
                        if(Bukkit.getPlayer(playerName) != null){
                            if(playerDataManager.can(player.getUniqueId(), Permission.CAN_INVITE_PLAYERS)){
                                if(playerDataManager.getByPlayerObj(Bukkit.getPlayer(playerName)).getDivision() == null){
                                    invitePlayer(player);
                                    chatFactory.sendPlayerMessage("You have invited " + playerName + " to your division!", true, player, prefix);
                                }else{
                                    chatFactory.sendPlayerMessage("This player is already in a division!", true, player, prefix);
                                }
                            }else{
                                chatFactory.sendPlayerMessage("You don't have this division permission!", true, player, prefix);
                            }
                        }else{
                            chatFactory.sendPlayerMessage("This player is either offline or isn't a valid player!", true, player, prefix);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You aren't in a division right now!", true, player, prefix);
                    }
                }else if(args[0].equalsIgnoreCase("kick") && args.length == 2){

                    String playerName = args[1];
                    if(player.getName().equalsIgnoreCase(playerName)){
                        chatFactory.sendPlayerMessage("You can't kick yourself!", true, player, prefix);
                        return false;
                    }

                    if(playerDataManager.getByPlayerObj(player).getDivision() != null){
                        if(Bukkit.getPlayer(playerName) != null) {
                            Player kickedPlayer = Bukkit.getOfflinePlayer(playerName).getPlayer();
                            if (playerDataManager.can(player.getUniqueId(), Permission.CAN_KICK_PLAYERS)) {
                                if (playerDataManager.getByPlayerObj(Bukkit.getPlayer(playerName)).getDivision() != null) {
                                    String kickerDivisionName = playerDataManager.getByPlayerObj(player).getDivision();
                                    String kickedPlayerDivisionName = playerDataManager.getByPlayerObj(kickedPlayer).getDivision();

                                    Rank kickerRank = playerDataManager.getByPlayerObj(player).getRank();
                                    Rank kickedPlayerRank = playerDataManager.getByPlayerObj(kickedPlayer).getRank();

                                    if(kickedPlayerRank == null){
                                        chatFactory.sendPlayerMessage("You can't kick the leader of the Division silly!", true, player, prefix);
                                        return false;
                                    }

                                    if (kickerDivisionName.equalsIgnoreCase(kickedPlayerDivisionName)) {
                                        //Kicker has a higher rank than kicked player
                                        if(Rank.isAHigherRank(kickerRank, kickedPlayerRank) == 1){
                                            Division division = divisionsManager.getDivision(kickerDivisionName);
                                            PlayerData kickedPlayerData = playerDataManager.getByPlayerObj(kickedPlayer);

                                            kickedPlayerData.setPlayerDivision(null);
                                            kickedPlayerData.setRank(null);
                                            division.removePlayer(kickedPlayerData);

                                            playerDataManager.setPlayerData(kickedPlayer.getUniqueId(), kickedPlayerData);
                                            divisionsManager.setDivision(kickerDivisionName, division);

                                            if(kickedPlayer.isOnline()){
                                                chatFactory.sendPlayerMessage("&cYou have been kicked from your division by &e" + player.getName(), true, player, prefix);
                                            }

                                        }else if(Rank.isAHigherRank(kickerRank, kickedPlayerRank) == 0){
                                            chatFactory.sendPlayerMessage("You can't kick a player that is the same rank as you!", true, player, prefix);
                                        }else{
                                            chatFactory.sendPlayerMessage("You can't kick a player that is a higher rank as you!", true, player, prefix);
                                        }
                                    }else{
                                        chatFactory.sendPlayerMessage("This player isn't in your division!", true, player, prefix);
                                    }
                                } else {
                                    chatFactory.sendPlayerMessage("This player isn't in your division!", true, player, prefix);
                                }
                            }else{
                                chatFactory.sendPlayerMessage("You don't have permission to kick players from your division!", true, player, prefix);
                            }
                        }else{
                            chatFactory.sendPlayerMessage("This player is either offline or isn't a valid player!", true, player, prefix);
                        }
                    }
                }else if(args[0].equalsIgnoreCase("edit")){

                    if(playerDataManager.isInDivision(player)){
                        if(args.length == 2){
                            if(args[1].equalsIgnoreCase("status")) {
                                if (playerDataManager.isInDivision(player)) {
                                    if (playerDataManager.can(player.getUniqueId(), Permission.CAN_ALTER_STATUS)) {

                                        String divisionName = playerDataManager.getByPlayerObj(player).getDivision();
                                        Division playerDivision = divisionsManager.getDivision(divisionName);
                                        DivisionStatus currentStatus = playerDivision.getStatus();

                                        if (currentStatus.equals(DivisionStatus.PRIVATE)) {
                                            playerDivision.setStatus(DivisionStatus.PUBLIC);
                                        } else {
                                            playerDivision.setStatus(DivisionStatus.PRIVATE);
                                        }

                                        divisionsManager.setDivision(divisionName, playerDivision);
                                        chatFactory.sendPlayerMessage("You have altered the division status to &e" + playerDivision.getStatus().name() + "!", true, player, prefix);
                                    }
                                    //divisions edit rank perm
                                }
                            }else if(args[1].equalsIgnoreCase("perms")) {
                                PermissionsMenu permissionsMenu = new PermissionsMenu(plugin);
                                permissionsMenu.initializeItems(player);
                                permissionsMenu.openInventory(player);
                            }
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You are not in a divison!", true, player, prefix);
                    }

                }else if((args[0].equalsIgnoreCase("promote") || args[0].equalsIgnoreCase("demote")) && args.length == 2){

                    String playerName = args[1];
                    if(playerDataManager.isInDivision(player)){

                        if(args[0].equalsIgnoreCase("promote")) {
                            if (!playerDataManager.can(player.getUniqueId(), Permission.CAN_PROMOTE_MEMBERS)) {
                                chatFactory.sendPlayerMessage("You do not have permission to promote members in your division!", true, player, prefix);
                                return false;
                            }
                        }else{
                            if(!playerDataManager.can(player.getUniqueId(), Permission.CAN_DEMOTE_MEMBERS)){
                                chatFactory.sendPlayerMessage("You do not have permission to demote members in your division!", true, player, prefix);
                                return false;
                            }
                        }

                        if(player.getName().equalsIgnoreCase(playerName)){
                            chatFactory.sendPlayerMessage("You can't kick yourself!", true, player, prefix);
                            return false;
                        }

                        if(PlayerHelper.isValidOnlinePlayer(playerName)){
                            Player playerAffected = Bukkit.getPlayer(playerName);
                            if(playerDataManager.isInDivision(playerAffected)){
                                if(playerDataManager.isInSameDivision(player, playerAffected)){

                                    PlayerData playerAffectedData = playerDataManager.getByPlayerObj(playerAffected);
                                    Rank playerAffectedRank = playerAffectedData.getRank();

                                    if(playerAffectedRank != null){
                                        Rank newRank;
                                        String playerAffectedMsg;
                                        String playerMsg;
                                        if(args[0].equalsIgnoreCase("promote")){
                                            newRank = Rank.getNextRank(playerAffectedRank);
                                            playerAffectedMsg = "You have been promoted to the rank &e" + newRank.name() + "!";
                                            playerMsg = "You have promoted &e" + playerAffected.getName() + " &fto the rank &e" + newRank.name() + "!";
                                        }else{
                                            newRank = Rank.getPreviousRank(playerAffectedRank);
                                            playerAffectedMsg = "You have been demoted to the rank &e" + newRank.name() + "!";
                                            playerMsg = "You have demoted &e" + playerAffected.getName() + " &fto the rank &e" + newRank.name() + "!";
                                        }
                                        playerAffectedData.setRank(newRank);
                                        playerDataManager.setPlayerData(playerAffected.getUniqueId(), playerAffectedData);

                                        chatFactory.sendPlayerMessage(playerAffectedMsg, true, playerAffected, prefix);
                                        chatFactory.sendPlayerMessage(playerMsg, true, player, prefix);
                                    }else{
                                        /*
                                            This person is owner
                                        */
                                        chatFactory.sendPlayerMessage("You can't do this to the leader of the division!", true, player, prefix);
                                    }

                                }else{
                                    chatFactory.sendPlayerMessage("This player is not in your division!", true, player, prefix);
                                }
                            }else{
                                chatFactory.sendPlayerMessage("This player is not in a division!", true, player, prefix);
                            }
                        }else{
                            chatFactory.sendPlayerMessage("This is not a valid online player!", true, player, prefix);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You are not in a divison!", true, player, prefix);
                    }

                }
            }
        }

        return false;
    }

    private void displayDivisionInfo(Division division, Player playerToSendTo){
        chatFactory.sendPlayerMessage("", false, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&aDivision info:", true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eName: &c" + division.getName(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&ePower: &c" + division.getPower(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eGold: &c" + division.getGoldAmount(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eLeader: &c" + division.getLeader().getPlayer().getName(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eKills: &c" + division.getKills(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eCasualties: &c" + division.getCasualties(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eNumber members: &c" + division.getPlayers().size(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eHearts destroyed: &c" + division.getHeartsDestroyed(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("&eShrines conquered: &c" + division.getShrinesConquered(), true, playerToSendTo, prefix);
        chatFactory.sendPlayerMessage("", false, playerToSendTo, prefix);
    }

    private void invitePlayer(Player inviter){

        String divisionName = playerDataManager.getByPlayerObj(inviter).getDivision();
        TextComponent msg = new TextComponent(chatFactory.colorString("You have been invited to the division &e" + divisionName + "&f by " + "&e" + inviter.getName() + ".&fIf you wish to accept this invite, press "));
        TextComponent join = new TextComponent(chatFactory.colorString("&aJoin"));
        TextComponent msg2 = new TextComponent(chatFactory.colorString(". If you wish to decline this invite, press "));
        TextComponent decline = new TextComponent(chatFactory.colorString("&cDecline"));

        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Join Division").create()));
        decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Decline invite").create()));
        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/divisionsconfirm invaccept " + divisionName));
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/divisionsconfirm invaccept abort"));

        msg.addExtra(join);
        msg.addExtra(msg2);
        msg.addExtra(decline);

    }



}
