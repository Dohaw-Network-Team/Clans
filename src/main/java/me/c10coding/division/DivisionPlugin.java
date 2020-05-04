package me.c10coding.division;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.c10coding.core.Core;
import me.c10coding.coreapi.CoreAPI;
import me.c10coding.coreapi.binder.Binder;
import me.c10coding.division.files.DivisionConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class DivisionPlugin extends JavaPlugin {

    private static Economy economy = null;
    //Used to fetch any other custom plugins within the "core" plugins.
    private Core core = new Core();
    //General API that benefits you no matter what plugin you're making.
    private CoreAPI api = core.getCoreAPI();

    @Inject
    private DivisionConfigManager dcm;

    final static String prefix = "&l[&aNewFactions&r&l]&r";

    @Override
    public void onEnable() {
        // Plugin startup logic

        Binder binder = api.getBinder(this);
        Injector injector = binder.createInjector();
        injector.injectMembers(this);

        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            this.getLogger().fine("Vault hooked!");
        }

        dcm.validateConfigs();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static String getPrefix(){
        return prefix;
    }

    public DivisionConfigManager getDivisionConfigManager(){
        return dcm;
    }
}
