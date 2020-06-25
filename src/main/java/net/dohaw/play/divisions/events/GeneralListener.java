package net.dohaw.play.divisions.events;

import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {

    private DivisionsPlugin plugin;
    private PlayerDataManager playerDataManager;

    public GeneralListener(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
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

}
