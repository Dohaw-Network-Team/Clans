package net.dohaw.play.divisions.utils;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.playerData.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DivisionChat {

    public static void sendChannelMessage(ChatFactory chatFactory, Division division, DivisionChannel channel, String msg){
        for(PlayerData pd : division.getPlayers()){

            OfflinePlayer op = pd.getPLAYER();
            if(op.isOnline() && pd.getChannel() == channel) {

                Player player = op.getPlayer();
                String channelName = chatFactory.firstUpperRestLower(channel.toString());

                if (channel != DivisionChannel.NONE) {
                    chatFactory.sendPlayerMessage(msg, true, player, "[" + channel.getPrefixColor() + channelName + "&f]");
                }

            }
        }
    }

    public static void sendAnnouncement(ChatFactory chatFactory, Division division, String msg){
        for(PlayerData pd : division.getPlayers()){
            OfflinePlayer op = pd.getPLAYER();
            if(op.isOnline()){

                Player player = op.getPlayer();

                chatFactory.sendCenteredMessage(player, "&l=== &b&lDIVISION ANNOUNCEMENT &f&l===");
                chatFactory.sendPlayerMessage(" ", false, player, null);
                chatFactory.sendPlayerMessage(msg, false, player, null);
                chatFactory.sendPlayerMessage(" ", false, player, null);
                chatFactory.sendCenteredMessage(player, "&l===============");

            }
        }
    }




}
