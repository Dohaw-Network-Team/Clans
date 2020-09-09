package net.dohaw.play.divisions.menus.itemcreation;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CreateItemMenu extends Menu {

    public CreateItemMenu(APIHook plugin, Menu previousMenu) {
        super(plugin, previousMenu,"Create Item", 45);
    }

    @Override
    public void initializeItems(Player p) {



    }

    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
