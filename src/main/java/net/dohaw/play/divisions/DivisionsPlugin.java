package net.dohaw.play.divisions;

import me.c10coding.coreapi.CoreAPI;
import net.dohaw.play.divisions.commands.ConfirmableCommands;
import net.dohaw.play.divisions.commands.DivisionsCommand;
import net.dohaw.play.divisions.events.GeneralListener;
import net.dohaw.play.divisions.files.DefaultPermConfig;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.runnables.InviteTimer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/*
    Divisions Plugin
    Author: c10coding
    Started: 6/16/2020
    Finished: ~
    Description: A better version of Factions
 */

public final class DivisionsPlugin extends JavaPlugin {

    private static Economy economy = null;
    //Used to fetch any other custom plugins within the "core" plugins.
    //private Core core;
    //General API that benefits you no matter what plugin you're making.
    private CoreAPI api;
    private String pluginPrefix;

    private DivisionsManager divisionsManager;
    private PlayerDataManager playerDataManager;
    private DefaultPermConfig defaultPermConfig;

    private HashMap<UUID, BukkitTask> invitedPlayers = new HashMap<>();

    @Override
    public void onEnable() {

        if(getServer().getPluginManager().getPlugin("CoreAPI") == null){
            getLogger().severe("Disabled due to no CoreAPI dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //this.core = (Core) getServer().getPluginManager().getPlugin("Core");
        this.api = (CoreAPI) getServer().getPluginManager().getPlugin("CoreAPI");

        if(api == null){
            getLogger().severe("Disabled due to no CoreAPI dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().fine("API hooked!");

        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().fine("Vault hooked!");

        this.pluginPrefix = getConfig().getString("PluginPrefix");

        validateConfigs();
        loadDefaultRankPermissions();
        loadManagerData();
        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable() {
        saveManagerData();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new GeneralListener(this), this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("divisions").setExecutor(new DivisionsCommand(this));
        getServer().getPluginCommand("divisionsconfirm").setExecutor(new ConfirmableCommands(this));
    }

    public CoreAPI getCoreAPI(){
        return api;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public void validateConfigs() {

        File playerDataFolder = new File(getDataFolder() + File.separator + "/playerData");
        File divisionsFolder = new File(getDataFolder() + File.separator + "/divisionsData");

        if(!playerDataFolder.exists()){
            if(playerDataFolder.mkdirs()){
                getLogger().info("Creating player data folder...");
            }
        }

        if(!divisionsFolder.exists()){
            if(divisionsFolder.mkdirs()){
                getLogger().info("Creating divisions data folder...");
            }
        }

        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "divisionsList.yml"), new File(getDataFolder(), "defaultPerms.yml")};
        for(File f : files){
            if(!f.exists()) {
                saveResource(f.getName(), false);
                getLogger().info("Loading " + f.getName() + "...");
            }
        }
    }

    public String getPluginPrefix(){
        return pluginPrefix;
    }

    public void loadManagerData(){
        divisionsManager = new DivisionsManager(this);
        playerDataManager = new PlayerDataManager(this);
        /*
        //Had to set DivisionsConfigGHandler after playerdatamanager because it's dependent on it
        divisionsManager.setDivisionsHandler(new DivisionsConfigHandler(this));*/
        playerDataManager.loadContents();
        divisionsManager.loadContents();
        playerDataManager.setPlayerDivisions();
    }

    public void saveManagerData(){
        if(divisionsManager != null){
            divisionsManager.saveContents();
        }

        if(playerDataManager != null){
            playerDataManager.saveContents();
        }
    }

    public void loadDefaultRankPermissions(){
        defaultPermConfig = new DefaultPermConfig(this);
        defaultPermConfig.compilePerms();
    }

    public DivisionsManager getDivisionsManager(){
        return divisionsManager;
    }

    public PlayerDataManager getPlayerDataManager(){
        return playerDataManager;
    }

    public DefaultPermConfig getDefaultPermConfig(){
        return defaultPermConfig;
    }

    public HashMap<UUID, BukkitTask> getInvitedPlayers(){
        return invitedPlayers;
    }

    public void addInvitedPlayer(UUID u){
        invitedPlayers.put(u, new InviteTimer(this, u).runTaskTimer(this, 1200L, 1200L));
    }

    public void removeInvitedPlayer(UUID u){
        invitedPlayers.get(u).cancel();
        invitedPlayers.remove(u);
    }

    public boolean hasBeenInvitedRecently(UUID u){
        return invitedPlayers.containsKey(u);
    }

}
