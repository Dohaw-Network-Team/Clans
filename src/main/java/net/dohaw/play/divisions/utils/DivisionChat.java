package net.dohaw.play.divisions.utils;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.playerData.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DivisionChat {

    public static void sendChannelMessage(ChatFactory chatFactory, DivisionChannel channel, String msg){

    }

    public static void sendAnnouncement(ChatFactory chatFactory, Division division, String msg){
        for(PlayerData pd : division.getPlayers()){
            OfflinePlayer op = pd.getPLAYER();
            if(op.isOnline()){
                Player player = op.getPlayer();
                
                chatFactory.sendPlayerMessage(msg, false, player, null);
            }
        }
    }

}
