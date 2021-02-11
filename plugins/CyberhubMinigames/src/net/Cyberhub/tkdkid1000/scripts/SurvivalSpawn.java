package net.Cyberhub.tkdkid1000.scripts;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class SurvivalSpawn implements Listener {

	private FileConfiguration config;

	public SurvivalSpawn(FileConfiguration config) {
		this.config = config;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("hub"))) {
			if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
				event.getPlayer().setGameMode(GameMode.SURVIVAL);
			}
			event.getPlayer().getInventory().clear();
			event.getPlayer().getActivePotionEffects().clear();
			event.getPlayer().setFoodLevel(20);
			event.getPlayer().setHealth(20);
		}
	}
}
