package net.dohaw.play.divisions.utils;

import org.bukkit.Bukkit;

public class PlayerHelper {

    public static boolean isValidOnlinePlayer(String playerName){
        return Bukkit.getPlayer(playerName) != null;
    }

}
