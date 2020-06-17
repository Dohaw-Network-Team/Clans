package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.ConfigManager;
import me.c10coding.coreapi.helpers.EnumHelper;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.rank.Rank;
import net.dohaw.play.divisions.rank.RankPermission;

import java.util.EnumMap;

public class DefaultPermConfig extends ConfigManager {

    private EnumMap<Rank, EnumMap<RankPermission, Object>> defaultPerms = new EnumMap<>(Rank.class);

    public DefaultPermConfig(DivisionsPlugin plugin) {
        super(plugin, "defaultperms");
    }

    public void compilePerms(){
        for(Rank rank : Rank.values()){
            defaultPerms.put(rank, getRankPermissions(rank));
        }
    }

    private EnumMap<RankPermission, Object> getRankPermissions(Rank rank){
        EnumHelper enumHelper = ((DivisionsPlugin)plugin).getCoreAPI().getEnumHelper();
        EnumMap<RankPermission, Object> defaultPermsForRank = new EnumMap<>(RankPermission.class);
        for(RankPermission perm : RankPermission.values()){
            Object value = config.get("Permissions." + enumHelper.enumToName(rank) + "." + enumHelper.enumToName(perm));
            defaultPermsForRank.put(perm, value);
        }
        return defaultPermsForRank;
    }

    public EnumMap<Rank, EnumMap<RankPermission, Object>> getDefaultPerms(){
        return defaultPerms;
    }

    public EnumMap<RankPermission, Object> getDefaultRankPermissions(Rank rank){
        return defaultPerms.get(rank);
    }



}
