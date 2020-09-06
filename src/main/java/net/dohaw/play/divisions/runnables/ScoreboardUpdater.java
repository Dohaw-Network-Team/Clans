package net.dohaw.play.divisions.runnables;

import me.c10coding.coreapi.chat.ChatFactory;
import net.dohaw.play.divisions.DivisionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardUpdater extends BukkitRunnable {

    private DivisionsPlugin plugin;
    private ChatFactory chatFactory;

    public ScoreboardUpdater(DivisionsPlugin plugin){
        this.plugin = plugin;
        this.chatFactory = plugin.getAPI().getChatFactory();
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
