package net.dohaw.play.divisions.runnables;

import lombok.Getter;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.PlayerData;
import net.dohaw.play.divisions.utils.Calculator;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Regener extends BukkitRunnable {

    @Getter private PlayerData data;
    @Getter private final UUID UUID;
    @Getter private DivisionsPlugin plugin;

    /*
        Regener error happens when i close game but not when i disconnect
     */
    public Regener(DivisionsPlugin plugin, final UUID UUID){
        this.UUID = UUID;
        this.plugin = plugin;
    }

    @Override
    public void run() {

        this.data = plugin.getPlayerDataManager().getPlayerByUUID(UUID);
        double playerRegen = data.getRegen();
        double maxRegen = Calculator.calculateMaxRegen(data);

        if(playerRegen != maxRegen){
            double finalPlayerRegen = playerRegen + Calculator.calculateRegen(data);
            if(maxRegen <= finalPlayerRegen){
                data.setRegen(playerRegen + Calculator.calculateRegen(data));
            }else{
                data.setRegen(maxRegen);
            }
        }

        plugin.getPlayerDataManager().updatePlayerData(UUID, data);
    }
}
