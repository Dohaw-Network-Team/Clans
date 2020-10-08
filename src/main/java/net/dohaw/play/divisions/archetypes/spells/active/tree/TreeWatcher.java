package net.dohaw.play.divisions.archetypes.spells.active.tree;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeWatcher extends BukkitRunnable {

    private Map<Location, Material> treeBlockLocations;
    private List<Location> mineableLogLocations;
    private Player spellCaster;
    private Location initLocation;

    public TreeWatcher(Player spellCaster, Location initLocation, List<Location> mineableLogLocations, Map<Location, Material> treeBlockLocations){
        this.mineableLogLocations = mineableLogLocations;
        this.treeBlockLocations = treeBlockLocations;
        this.spellCaster = spellCaster;
        this.initLocation = initLocation;
    }

    @Override
    public void run() {
        if(hasMinedAllLogs(mineableLogLocations)){
            removeTree(treeBlockLocations);
            spellCaster.setGameMode(GameMode.SURVIVAL);
            spellCaster.teleport(initLocation);
            this.cancel();
        }
    }

    public static void removeTree(Map<Location, Material> treeBlockLocations){
        for(Map.Entry<Location, Material> previousLoc : treeBlockLocations.entrySet()){
            Location loc = previousLoc.getKey();
            Material mat = previousLoc.getValue();
            loc.getBlock().setType(mat);
        }
    }

    public boolean hasMinedAllLogs(List<Location> mineableLogLocations){
        for(Location loc : mineableLogLocations){
            if(loc.getBlock().getType() != Material.AIR){
                return false;
            }
        }
        return true;
    }

}
