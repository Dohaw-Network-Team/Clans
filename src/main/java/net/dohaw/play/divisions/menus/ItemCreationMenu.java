package net.dohaw.play.divisions.menus;

import me.c10coding.coreapi.APIHook;
import me.c10coding.coreapi.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemCreationMenu extends Menu {

    public ItemCreationMenu(APIHook plugin) {
        super(plugin, "Item Creation", 45);
    }

    @Override
    public void initializeItems(Player p) {

    }

    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
