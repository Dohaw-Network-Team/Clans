package net.dohaw.play.divisions.runnables;

import net.dohaw.play.divisions.DivisionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdater extends BukkitRunnable {

    private DivisionsPlugin plugin;

    public ScoreboardUpdater(DivisionsPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for(Player p : Bukkit.getOnlinePlayers()){

        }

    }
/*
    public void setSB(Player player){

        ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("Sidebar", "dummy");

        String displayName = "=== &b&lDohaw &e&lNetwork&f ===";
        obj.setDisplayName(displayName);
        obj.setDisplaySlot(DisplaySlot);


    }*/

}
