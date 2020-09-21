package net.dohaw.play.divisions.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class EntityUtils {

    public static boolean isValidOnlinePlayer(String playerName){
        return Bukkit.getPlayer(playerName) != null;
    }

    public static Entity getPotentialPlayerFromProjectile(Entity entity){
        ProjectileSource source = ((Projectile) entity).getShooter();
        return source instanceof Entity ? (Entity) source : null;
    }

    /*
        If the entity is a projectile, it gets the shooter.
        If the entity isn't a projectile, it checks to see if it's a player.
     */
    public static boolean isEntityInvolvedAPlayer(Entity entityInvolved){
        if(entityInvolved instanceof Player){
            return true;
        }else{
            if(entityInvolved instanceof Projectile){
                if(getPotentialPlayerFromProjectile(entityInvolved) != null){
                    Entity potentialPlayer = getPotentialPlayerFromProjectile(entityInvolved);
                    return potentialPlayer instanceof Player;
                }
            }
        }
        return false;
    }

}
