package net.dohaw.play.divisions.utils;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DivisionChat {

    public static void sendChannelMessage(PlayerDataManager playerDataManager, ChatFactory chatFactory, Division division, DivisionChannel channel, String msg){
        for(UUID uuid : division.getPlayers()){

            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            if(op.isOnline()) {

                PlayerData pd = playerDataManager.getPlayerByUUID(uuid);
                if(pd.getChannel() == channel){
                    Player player = op.getPlayer();
                    String channelName = chatFactory.firstUpperRestLower(channel.toString());

                    if (channel != DivisionChannel.NONE) {
                        chatFactory.sendPlayerMessage(msg, true, player, "[" + channel.getPrefixColor() + channelName + "&f]");
                    }
                }

            }
        }
    }

    public static void sendAnnouncement(ChatFactory chatFactory, Division division, String msg){

        for(UUID uuid : division.getPlayers()){

            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            if(op.isOnline()){

                Player player = op.getPlayer();
                chatFactory.sendPlayerMessage(" ", false, player, null);
                chatFactory.sendCenteredMessage(player, "&l=== &b&lDIVISION ANNOUNCEMENT &f&l===");
                chatFactory.sendPlayerMessage(" ", false, player, null);
                chatFactory.sendCenteredMessage(player, msg);
                chatFactory.sendPlayerMessage(" ", false, player, null);
                chatFactory.sendCenteredMessage(player, "&l===============");

            }

        }

    }

    public static void sendMOTD(ChatFactory chatFactory, Division division, Player player){
        String motd = division.getMotd();
        if(motd != null){
            chatFactory.sendCenteredMessage(player, "===== MOTD =====");
            chatFactory.sendPlayerMessage(" ", false, player, null);
            chatFactory.sendCenteredMessage(player, motd);
            chatFactory.sendPlayerMessage(" ", false, player, null);
            chatFactory.sendCenteredMessage(player, "===============");
        }
    }




}
