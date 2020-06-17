package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DivisionsCommand implements CommandExecutor {

    private DivisionsPlugin plugin;
    private DivisionsManager divisionsManager;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private String prefix;

    public DivisionsCommand(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.divisionsManager = plugin.getDivisionsManager();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.chatFactory = plugin.getCoreAPI().getChatFactory();
        this.prefix = plugin.getPluginPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length > 0){
                //divions create <NAME> <PUBLIC | PRIVATE>
                if(args[0].equalsIgnoreCase("create") && args.length == 3){
                    String divisionName = args[1];
                    if(!divisionsManager.hasContent(divisionName)){
                        divisionsManager.createNewDivision(divisionName, player);
                        playerDataManager.getByPlayerObj(player).setPlayerDivision(divisionsManager.getDivision(divisionName));
                        chatFactory.sendPlayerMessage("Created a new division called &a&l" + divisionName + "!", true, player, prefix);
                    }else{
                        chatFactory.sendPlayerMessage("This is already a division!", true, player, prefix);
                    }
                }
            }
        }

        return false;
    }
}
