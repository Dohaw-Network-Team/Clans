package net.dohaw.play.divisions.runnables;

import lombok.Getter;
import net.dohaw.play.divisions.DivisionsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class InviteTimer extends BukkitRunnable {

    private DivisionsPlugin plugin;
    //Minutes
    private UUID playerUUID;
    @Getter private int inviteCooldown;

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
