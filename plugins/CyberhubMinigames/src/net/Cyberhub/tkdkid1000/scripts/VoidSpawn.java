package net.Cyberhub.tkdkid1000.scripts;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidSpawn implements Listener {

	private FileConfiguration config;

	public VoidSpawn(FileConfiguration config) {
		this.config = config;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("hub"))) {
			if (event.getPlayer().getLocation().getY() < 1) {
				event.getPlayer().performCommand("spawn");
			}
		}
	}
}
