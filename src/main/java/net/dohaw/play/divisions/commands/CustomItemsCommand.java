package net.dohaw.play.divisions.commands;

import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.menus.itemcreation.CreateItemMenu;
import net.dohaw.play.divisions.menus.itemcreation.CustomItemsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomItemsCommand implements CommandExecutor {

    private DivisionsPlugin plugin;

    public CustomItemsCommand(DivisionsPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player pSender = (Player) sender;
            CustomItemsMenu customItemsMenu = new CustomItemsMenu(plugin);
            customItemsMenu.initializeItems(pSender);
            customItemsMenu.openInventory(pSender);
        }

        return false;
    }
}
