package net.Cyberhub.tkdkid1000;

import org.bukkit.plugin.java.JavaPlugin;

public class CyberhubKitpvp extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MoneyKill(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
