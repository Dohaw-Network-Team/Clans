package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.ArchetypeName;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArchetypesCommand implements CommandExecutor {

    private DivisionsPlugin plugin;
    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;

    public ArchetypesCommand(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args[0].equalsIgnoreCase("set") && args.length == 3){

            String playerName = args[1];
            String archetypeAlias = args[2];

            if(Bukkit.getPlayer(playerName) != null){
                if(ArchetypeName.getByAlias(archetypeAlias) != null){

                    Player player = Bukkit.getPlayer(playerName);
                    ArchetypeName archetypeName = ArchetypeName.getByAlias(archetypeAlias);

                    UUID playerUUID = player.getUniqueId();
                    PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);

                    if(pd.getArchetype() == null){

                    }else{
                        chatFactory.sendPlayerMessage("This player already has an archetype!", true, sender, plugin.getPluginPrefix());
                    }

                }
            }else{
                chatFactory.sendPlayerMessage("This player either isn't online or isn't valid!", true, sender, plugin.getPluginPrefix());
            }

        }
        return false;
    }
}
