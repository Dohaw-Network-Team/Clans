package net.dohaw.play.divisions.utils;

import net.dohaw.play.corelib.menus.Menu;
import org.bukkit.entity.Player;

public class MenuHelper {

    public static void goToPreviousMenu(Menu previousMenu, Player player){
        previousMenu.initializeItems(player);
        previousMenu.openInventory(player);
    }

}
