package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import org.bukkit.Bukkit;
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
                //divisions create <NAME> <PUBLIC | PRIVATE>
                if(args[0].equalsIgnoreCase("create") && args.length == 3){
                    String divisionName = args[1];
                    String status = args[2];
                    if(playerDataManager.getByPlayerObj(player).getDivision() == null){
                        if(!divisionsManager.hasContent(divisionName)){
                            if(status.equalsIgnoreCase("public") || status.equalsIgnoreCase("private")){
                                divisionsManager.createNewDivision(divisionName, player);
                                playerDataManager.getByPlayerObj(player).setPlayerDivision(divisionsManager.getDivision(divisionName));
                                playerDataManager.getByPlayerObj(player).setRank(null);
                                chatFactory.sendPlayerMessage("Created a new division called &a&l" + divisionName + "!", true, player, prefix);
                            }else{
                                chatFactory.sendPlayerMessage("Your division can only be a public or private division.", true, player, prefix);
                            }
                        }else{
                            chatFactory.sendPlayerMessage("This is already a division!", true, player, prefix);
                        }
                    }else{
                        chatFactory.sendPlayerMessage("You are already in a division!", true, player, prefix);
                    }
                }
            }
        }

        return false;
    }
}
