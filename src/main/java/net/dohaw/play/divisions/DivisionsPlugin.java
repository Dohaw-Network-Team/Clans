package net.dohaw.play.divisions;

import me.c10coding.core.Core;
import me.c10coding.coreapi.CoreAPI;
import net.dohaw.play.divisions.files.DefaultPermConfig;
import net.dohaw.play.divisions.files.DivisionConfigManager;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
    private Core core;
    //General API that benefits you no matter what plugin you're making.
    private CoreAPI api;
    private String pluginPrefix;

    private DivisionsManager divisionsManager;
    private PlayerDataManager playerDataManager;
    private DivisionConfigManager divisionConfigManager;
    private DefaultPermConfig defaultPermConfig;

    @Override
    public void onEnable() {

        if(getServer().getPluginManager().getPlugin("Core") == null){
            getLogger().severe("Disabled due to no Core dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.core = (Core) getServer().getPluginManager().getPlugin("Core");
        this.api = core.getCoreAPI();

        if(api == null){
            getLogger().severe("Disabled due to no CoreAPI dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().fine("API hooked!");

        validateConfigs();
        loadManagerData();
        loadDefaultRankPermissions();

        this.pluginPrefix = getConfig().getString("PluginPrefix");

        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().fine("Vault hooked!");

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

    public Core getCore() {
        return core;
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
            playerDataFolder.mkdirs();
        }

        if(!divisionsFolder.exists()){
            divisionsFolder.mkdirs();
        }

        File[] files = {new File(getDataFolder(), "config.yml"), new File(getDataFolder(), "divisionsList.yml")};
        for(File f : files){
            if(!f.exists()) {
                saveResource(f.getName(), false);
                Bukkit.getConsoleSender().sendMessage(getPluginPrefix() + " Loading " + f.getName());
            }
        }
    }

    public String getPluginPrefix(){
        return pluginPrefix;
    }

    public void loadManagerData(){

        this.divisionsManager = new DivisionsManager();
        this.playerDataManager = new PlayerDataManager();
        this.divisionConfigManager = new DivisionConfigManager();

        divisionConfigManager.loadContents();
        playerDataManager.loadContents();
        divisionConfigManager.loadContents();

    }

    public void loadDefaultRankPermissions(){
        this.defaultPermConfig = new DefaultPermConfig(this);
        defaultPermConfig.compilePerms();
    }

    public void saveManagerData(){
        divisionConfigManager.saveContents();
        playerDataManager.saveContents();
        divisionConfigManager.saveContents();
    }

    public DivisionsManager getDivisionsManager(){
        return divisionsManager;
    }

    public PlayerDataManager getPlayerDataManager(){
        return playerDataManager;
    }

    public DivisionConfigManager getDivisionConfigManager(){
        return divisionConfigManager;
    }


}
