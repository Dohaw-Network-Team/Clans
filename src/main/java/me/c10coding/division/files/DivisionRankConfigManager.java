package me.c10coding.division.files;

import me.c10coding.coreapi.files.ConfigManager;
import me.c10coding.division.DivisionPlugin;
import me.c10coding.division.ranks.Rank;
import me.c10coding.division.ranks.RankPermission;

public class DivisionRankConfigManager extends ConfigManager {


    public DivisionRankConfigManager(DivisionPlugin plugin) {
        super(plugin, "ranks.yml");
    }

    @Override
    public void validateConfigs() { }

    public void setSetting(RankPermission rp){
        String configValue = ((DivisionPlugin) plugin).
    }

    public void validateRanks(){
        for(Rank r : Rank.values()){
            for(RankPermission rp : RankPermission.values()){
                
            }
        }
    }
}
