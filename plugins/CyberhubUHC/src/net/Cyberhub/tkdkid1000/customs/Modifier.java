package net.Cyberhub.tkdkid1000.customs;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import net.Cyberhub.tkdkid1000.CyberhubUHC;

public class Modifier implements Listener {

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, CyberhubUHC.getMain().getPlugin());
	}
	public void enable() {
		
	}
	
	boolean enabled = false;
}
