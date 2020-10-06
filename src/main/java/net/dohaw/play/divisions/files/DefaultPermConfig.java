package net.dohaw.play.divisions.files;

import net.dohaw.corelib.Config;
import net.dohaw.corelib.helpers.EnumHelper;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;

import java.util.EnumMap;

public class DefaultPermConfig extends Config {

    private EnumMap<Rank, EnumMap<Permission, Object>> defaultPerms = new EnumMap<>(Rank.class);

    public DefaultPermConfig(DivisionsPlugin plugin) {
        super(plugin, "defaultPerms.yml");
    }

    public void compilePerms(){
        for(Rank rank : Rank.values()){
            defaultPerms.put(rank, getRankPermissions(rank));
        }
    }

    private EnumMap<Permission, Object> getRankPermissions(Rank rank){
        EnumMap<Permission, Object> defaultPermsForRank = new EnumMap<>(Permission.class);
        for(Permission perm : Permission.values()){
            Object value = config.get("Permissions." + EnumHelper.enumToName(rank) + "." + EnumHelper.enumToName(perm));
            defaultPermsForRank.put(perm, value);
        }
        return defaultPermsForRank;
    }

    public EnumMap<Rank, EnumMap<Permission, Object>> getDefaultPerms(){
        return defaultPerms;
    }

    public EnumMap<Permission, Object> getDefaultRankPermissions(Rank rank){
        return defaultPerms.get(rank);
    }



}
