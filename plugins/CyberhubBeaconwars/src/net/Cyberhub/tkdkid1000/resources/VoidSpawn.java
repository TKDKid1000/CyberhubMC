package net.Cyberhub.tkdkid1000.resources;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;

public class VoidSpawn implements Listener {

	private FileConfiguration config;
	private CyberhubBeaconwars beacon;

	public VoidSpawn(CyberhubBeaconwars beacon, FileConfiguration config) {
		this.config = config;
		this.beacon = beacon;
	}
	
	public void register() {
		beacon.getServer().getPluginManager().registerEvents(this, beacon);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("spawnworld"))) return;
		if (event.getPlayer().getLocation().getY() < 110) {
			event.getPlayer().performCommand("spawn");
		}
	}
}
