package net.dohaw.play.divisions.utils;

import me.c10coding.coreapi.menus.Menu;
import org.bukkit.entity.Player;

public class MenuHelper {

    public static void goToPreviousMenu(Menu previousMenu, Player player){
        previousMenu.initializeItems(player);
        previousMenu.openInventory(player);
    }

}
