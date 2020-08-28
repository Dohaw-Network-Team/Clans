package net.dohaw.play.divisions.events;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.division.Division;
import net.dohaw.play.divisions.events.custom.NewMemberEvent;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.playerData.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {

    private PlayerDataManager playerDataManager;
    private ChatFactory chatFactory;

    public GeneralListener(DivisionsPlugin plugin){
        this.playerDataManager = plugin.getPlayerDataManager();
        this.chatFactory = plugin.getAPI().getChatFactory();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(playerDataManager.getByPlayerObj(p) == null){
            playerDataManager.addPlayerData(p.getUniqueId());
            playerDataManager.setPlayerDivision(playerDataManager.getPlayerByUUID(p.getUniqueId()));
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

        Division division = e.getDivision();
        PlayerData pd = e.getNewMember();
        OfflinePlayer op = pd.getPLAYER();

        if(op.isOnline()){

        }

    }

}
