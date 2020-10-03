package net.dohaw.play.divisions;

import lombok.Getter;
import me.c10coding.coreapi.BetterJavaPlugin;
import net.dohaw.play.divisions.archetypes.Archetype;
import net.dohaw.play.divisions.archetypes.specializations.Speciality;
import net.dohaw.play.divisions.archetypes.spells.Spell;
import net.dohaw.play.divisions.archetypes.spells.SpellWrapper;
import net.dohaw.play.divisions.commands.ArchetypesCommand;
import net.dohaw.play.divisions.commands.ConfirmableCommands;
import net.dohaw.play.divisions.commands.CustomItemsCommand;
import net.dohaw.play.divisions.commands.DivisionsCommand;
import net.dohaw.play.divisions.customitems.CustomItem;
import net.dohaw.play.divisions.events.GeneralListener;
import net.dohaw.play.divisions.events.ProgressListener;
import net.dohaw.play.divisions.files.DefaultConfig;
import net.dohaw.play.divisions.files.DefaultPermConfig;
import net.dohaw.play.divisions.files.MessagesConfig;
import net.dohaw.play.divisions.files.StatsConfig;
import net.dohaw.play.divisions.managers.CustomItemManager;
import net.dohaw.play.divisions.managers.DivisionsManager;
import net.dohaw.play.divisions.managers.PlayerDataManager;
import net.dohaw.play.divisions.runnables.InviteTimer;
import net.dohaw.play.divisions.utils.Calculator;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/*
    Divisions Plugin
    Author: c10coding
    Started: 6/16/2020
    Finished: 8/30/2020 (Finished 95% of features. Got to do the rest later. Took a break for a while which is why there's a 2 month gap.)
    Description: A better version of Factions specifically made for Dohaw Network
 */

public final class DivisionsPlugin extends BetterJavaPlugin {

    @Getter private static DivisionsPlugin instance;
    @Getter private static Economy economy = null;
    @Getter private String pluginPrefix;

    @Getter private DivisionsManager divisionsManager;
    @Getter private PlayerDataManager playerDataManager;
    @Getter private CustomItemManager customItemManager;
    @Getter private DefaultPermConfig defaultPermConfig;
    @Getter private MessagesConfig messagesConfig;
    @Getter private StatsConfig statsConfig;
    @Getter private DefaultConfig defaultConfig;

    @Getter private HashMap<UUID, BukkitTask> invitedPlayers = new HashMap<>();

    @Override
    public void onEnable() {

        hookAPI(this);
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().fine("Vault hooked!");

        validateConfigs();
        this.pluginPrefix = getConfig().getString("Plugin Prefix");
        this.messagesConfig = new MessagesConfig(this);
        this.statsConfig = new StatsConfig(this);
        this.defaultConfig = new DefaultConfig(this);
        Calculator.setDefaultConfig(defaultConfig);

        loadDefaultRankPermissions();

        /*
            Just puts the static contents into a map so that it's easy to filter specific things.
         */
        registerArchetypes();
        registerSpecialities();
        registerSpells();

        loadManagerData();

        registerEvents(new GeneralListener(this), new ProgressListener(this));
        registerCommand("divisions", new DivisionsCommand(this));
        registerCommand("divisionsconfirm", new ConfirmableCommands(this));
        registerCommand("archetypes", new ArchetypesCommand(this));
        registerCommand("customitems", new CustomItemsCommand(this));

    }

    @Override
    public void onDisable() {
        saveManagerData();
    }

    private void registerArchetypes(){
        Archetype.registerWrapper(Archetype.ASSASSIN);
        Archetype.registerWrapper(Archetype.ARCHER);
        Archetype.registerWrapper(Archetype.CRUSADER);
        Archetype.registerWrapper(Archetype.DUELIST);
        Archetype.registerWrapper(Archetype.EVOKER);
        Archetype.registerWrapper(Archetype.WIZARD);
        Archetype.registerWrapper(Archetype.CLERIC);
        Archetype.registerWrapper(Archetype.TREE);
    }

    private void registerSpecialities(){
        Speciality.registerWrapper(Speciality.CONTROL);
        Speciality.registerWrapper(Speciality.DECEPTION);
        Speciality.registerWrapper(Speciality.SOUL_PIERCING);
        Speciality.registerWrapper(Speciality.PROACTIVE);
        Speciality.registerWrapper(Speciality.SHADOW);
        Speciality.registerWrapper(Speciality.VENOM);
        Speciality.registerWrapper(Speciality.DIRECT);
        Speciality.registerWrapper(Speciality.SPREAD);
        Speciality.registerWrapper(Speciality.VAMPIRIC);
        Speciality.registerWrapper(Speciality.ORDER);
        Speciality.registerWrapper(Speciality.PROTECTION);
        Speciality.registerWrapper(Speciality.PSYCHOTIC);
        Speciality.registerWrapper(Speciality.SOUL);
        Speciality.registerWrapper(Speciality.UNIFORM);
        Speciality.registerWrapper(Speciality.CONSCIOUS);
        Speciality.registerWrapper(Speciality.DESTRUCTION);
        Speciality.registerWrapper(Speciality.ELEMENTAL);
        Speciality.registerWrapper(Speciality.FIRE);
        Speciality.registerWrapper(Speciality.ICE);
        Speciality.registerWrapper(Speciality.TEMPEST);
    }

    private void registerSpells(){
        Spell.registerWrapper(Spell.FROST_STRIKE);
        Spell.registerWrapper(Spell.INVISIBLE_STRIKE);
        Spell.registerWrapper(Spell.SMITE);
        Spell.registerWrapper(Spell.HEATING_UP);
        Spell.registerWrapper(Spell.ESCAPE);
        Spell.registerWrapper(Spell.CRIPPLING_SHOT);
        Spell.registerWrapper(Spell.SPORADIC);
        Spell.registerWrapper(Spell.STUN);
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

        File[] files = {
            new File(getDataFolder(), "config.yml"),
            new File(getDataFolder(), "divisionsList.yml"),
            new File(getDataFolder(), "defaultPerms.yml"),
            new File(getDataFolder(), "stats.yml"),
            new File(getDataFolder(), "customItems.yml"),
            new File(getDataFolder(), "messages.yml")
        };

        for(File f : files){
            if(!f.exists()) {
                saveResource(f.getName(), false);
                getLogger().info("Loading " + f.getName() + "...");
            }
        }
    }

    public void loadManagerData(){

        divisionsManager = new DivisionsManager(this);
        playerDataManager = new PlayerDataManager(this);
        customItemManager = new CustomItemManager(this);

        playerDataManager.loadContents();
        divisionsManager.loadContents();
        customItemManager.loadContents();

        playerDataManager.setPlayerDivisions();
    }

    public void saveManagerData(){

        if(divisionsManager != null){
            divisionsManager.saveContents();
        }

        if(playerDataManager != null){
            playerDataManager.saveContents();
        }

        if(customItemManager != null){
            customItemManager.saveContents();
        }

    }

    public void loadDefaultRankPermissions(){
        defaultPermConfig = new DefaultPermConfig(this);
        defaultPermConfig.compilePerms();
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

    public void reload(){
        defaultConfig.reloadConfig();
        statsConfig.reloadConfig();
        defaultPermConfig.reloadConfig();
        messagesConfig.reloadConfig();
    }

    public static DivisionsPlugin getInstance(){
        return instance;
    }

}
