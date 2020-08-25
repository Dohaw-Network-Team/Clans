package net.dohaw.play.divisions.files;

import me.c10coding.coreapi.files.ConfigManager;
import me.c10coding.coreapi.helpers.EnumHelper;
import net.dohaw.play.divisions.DivisionsPlugin;
import net.dohaw.play.divisions.rank.Permission;
import net.dohaw.play.divisions.rank.Rank;

import java.util.EnumMap;

public class DefaultPermConfig extends ConfigManager {

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
        EnumHelper enumHelper = ((DivisionsPlugin)plugin).getCoreAPI().getEnumHelper();
        EnumMap<Permission, Object> defaultPermsForRank = new EnumMap<>(Permission.class);
        for(Permission perm : Permission.values()){
            Object value = config.get("Permissions." + enumHelper.enumToName(rank) + "." + enumHelper.enumToName(perm));
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
