package me.caleb.Clan;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.caleb.Clan.commands.Commands;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

	private static Main instance;
	public static Economy economy = null;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
        	this.getLogger().fine("Vault hooked!");
        }
		
		this.saveResource("clans.yml", false);
		this.saveResource("config.yml", false);
		
		new Commands(this);
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static Main getPlugin() {
		return instance;
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
	
}
