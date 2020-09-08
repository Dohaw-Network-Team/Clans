package net.dohaw.play.divisions.commands;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.ArchetypeWrapper;
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
                if(Archetype.getByAlias(archetypeAlias) != null){

                    Player player = Bukkit.getPlayer(playerName);
                    ArchetypeWrapper archetype = Archetype.getByAlias(archetypeAlias);

                    UUID playerUUID = player.getUniqueId();
                    PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);

                    if(pd.getArchetype() == null){

                        pd.setArchetype(archetype);
                        playerDataManager.updatePlayerData(playerUUID, pd);

                        chatFactory.sendPlayerMessage("You have given this player the archetype " + archetype.getName() + "!", true, sender, plugin.getPluginPrefix());

                    }else{
                        chatFactory.sendPlayerMessage("This player already has an archetype!", true, sender, plugin.getPluginPrefix());
                    }

                }
            }else{
                chatFactory.sendPlayerMessage("This player either isn't online or isn't valid!", true, sender, plugin.getPluginPrefix());
            }

        }else if(args[0].equalsIgnoreCase("reset") && args.length <= 2){
            if(args.length == 2){
                String playerName = args[1];
                if(Bukkit.getPlayer(playerName) != null){

                    Player player = Bukkit.getPlayer(playerName);
                    UUID playerUUID = player.getUniqueId();
                    PlayerData pd = playerDataManager.getPlayerByUUID(playerUUID);

                    resetArchetype(pd);

                }else{
                    chatFactory.sendPlayerMessage("This player either isn't online or isn't valid!", true, sender, plugin.getPluginPrefix());
                }
            }else{
                if(sender instanceof Player){
                    Player pSender = (Player) sender;
                    PlayerData pd = playerDataManager.getPlayerByUUID(pSender.getUniqueId());
                    resetArchetype(pd);
                }else{
                    chatFactory.sendPlayerMessage("Only players can do this to themselves!", true, sender, plugin.getPluginPrefix());
                }
            }
        }
        return false;
    }

    private void resetArchetype(PlayerData pd){

        if(pd.getArchetype() != null){
            pd.setArchetype(null);
            playerDataManager.updatePlayerData(pd.getPLAYER_UUID(), pd);
            chatFactory.sendPlayerMessage("You have reset your archetype as well as your archetype stats!", true, pd.getPLAYER().getPlayer(), plugin.getPluginPrefix());
        }else{
            chatFactory.sendPlayerMessage("You don't have an archetype right now!", true, pd.getPLAYER().getPlayer(), plugin.getPluginPrefix());
        }

    }

}
