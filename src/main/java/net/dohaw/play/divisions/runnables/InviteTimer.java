package net.dohaw.play.divisions.runnables;

import net.dohaw.play.divisions.DivisionsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class InviteTimer extends BukkitRunnable {

    private DivisionsPlugin plugin;
    //Minutes
    private int inviteCooldown;
    private UUID playerUUID;

    public InviteTimer(DivisionsPlugin plugin, UUID playerUUID){
        this.plugin = plugin;
        this.inviteCooldown = plugin.getConfig().getInt("Invite Cooldown");
        this.playerUUID = playerUUID;
    }

    /*
        The player has 5 minutes to accept or decline the invite
     */
    @Override
    public void run() {
        if(inviteCooldown != 0){
            inviteCooldown--;
        }else{
            plugin.removeInvitedPlayer(playerUUID);
        }
    }
}
