package net.dohaw.play.divisions.utils;

import net.dohaw.corelib.ChatSender;
import net.dohaw.corelib.StringUtils;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DivisionChat {

    public static void sendChannelMessage(PlayerDataManager playerDataManager, Division division, DivisionChannel channel, String msg){
        for(UUID uuid : division.getPlayers()){

            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            if(op.isOnline()) {

                PlayerData pd = playerDataManager.getPlayerByUUID(uuid);
                if(pd.getChannel() == channel){
                    Player player = op.getPlayer();
                    String channelName = StringUtils.firstUpperRestLower(channel.toString());

                    if (channel != DivisionChannel.NONE) {
                        ChatSender.sendPlayerMessage(msg, true, player, "[" + channel.getPrefixColor() + channelName + "&f]");
                    }
                }

            }
        }
    }

    public static void sendAnnouncement(Division division, String msg){

        for(UUID uuid : division.getPlayers()){

            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            if(op.isOnline()){

                Player player = op.getPlayer();
                ChatSender.sendPlayerMessage(" ", false, player, null);
                ChatSender.sendCenteredMessage(player, "&l=== &b&lDIVISION ANNOUNCEMENT &f&l===");
                ChatSender.sendPlayerMessage(" ", false, player, null);
                ChatSender.sendCenteredMessage(player, msg);
                ChatSender.sendPlayerMessage(" ", false, player, null);
                ChatSender.sendCenteredMessage(player, "&l===============");

            }

        }

    }

    public static void sendMOTD(Division division, Player player){
        String motd = division.getMotd();
        if(motd != null){
            ChatSender.sendCenteredMessage(player, "===== MOTD =====");
            ChatSender.sendPlayerMessage(" ", false, player, null);
            ChatSender.sendCenteredMessage(player, motd);
            ChatSender.sendPlayerMessage(" ", false, player, null);
            ChatSender.sendCenteredMessage(player, "===============");
        }
    }




}
