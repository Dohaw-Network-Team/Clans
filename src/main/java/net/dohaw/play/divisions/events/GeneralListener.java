package net.dohaw.play.divisions.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionChannel;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.events.custom.NewMemberEvent;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import net.dohaw.play.divisions.utils.DivisionChat;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class GeneralListener implements Listener {

    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;
    private DivisionsManager divisionsManager;

    public GeneralListener(DivisionsPlugin plugin){
        this.playerDataManager = plugin.getPlayerDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
        this.divisionsManager = plugin.getDivisionsManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        if(playerDataManager.getByPlayerObj(player) == null){
            playerDataManager.addPlayerData(player.getUniqueId());
            playerDataManager.setPlayerDivision(playerDataManager.getPlayerByUUID(player.getUniqueId()));
        }

        PlayerData pd = playerDataManager.getPlayerByUUID(player.getUniqueId());

        if(pd.getDivision() != null){
            Division division = divisionsManager.getDivision(pd.getDivision());
            DivisionChat.sendMOTD(chatFactory, division, player);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        if(playerDataManager.getPlayerByUUID(e.getPlayer().getUniqueId()) != null){
            playerDataManager.removePlayerData(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onAdditionOfMember(NewMemberEvent e){

        PlayerData pd = e.getNewMember();
        OfflinePlayer op = pd.getPLAYER();

        if(op.isOnline()){
            Player player = op.getPlayer();
            Division division = e.getDivision();
            DivisionChat.sendMOTD(chatFactory, division, player);
        }

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerData pd = playerDataManager.getPlayerByUUID(uuid);

        if(pd.getDivision() != null){
            DivisionChannel channel = pd.getChannel();
            if(channel != DivisionChannel.NONE){

                e.setCancelled(true);
                Division division = divisionsManager.getDivision(pd.getDivision());
                List<PlayerData> members = division.getPlayers();

                String msg = "<" + player.getName() + "> " + "&7" + e.getMessage();
                for(PlayerData pData : members){

                    DivisionChannel pChannel = pData.getChannel();
                    OfflinePlayer op = pData.getPLAYER();

                    if(op.isOnline()){
                        if(pChannel == channel){
                            String channelPrefix = channel.getPrefix();
                            Player recipient = op.getPlayer();
                            chatFactory.sendPlayerMessage(msg, true, recipient, channelPrefix);
                        }
                    }

                }

            }
        }

    }

}
