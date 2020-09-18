package net.dohaw.play.divisions.runnables;

import lombok.Getter;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.scheduler.BukkitRunnable;

public class Regener extends BukkitRunnable {

    @Getter private PlayerData data;
    @Getter private DivisionsPlugin plugin;

    public Regener(DivisionsPlugin plugin, PlayerData data){
        this.data = data;
        this.plugin = plugin;
    }

    @Override
    public void run() {

        double playerRegen = data.getRegen();
        double maxRegen = Calculator.calculateMaxRegen(data);
        if(playerRegen != maxRegen){
            double finalPlayerRegen = playerRegen + Calculator.calculateRegen(data);
            if(maxRegen <= finalPlayerRegen){
                data.setRegen(data.getRegen() + Calculator.calculateRegen(data));
            }else{
                data.setRegen(maxRegen);
            }
        }

        plugin.getPlayerDataManager().updatePlayerData(data.getPLAYER_UUID(), data);
    }
}
