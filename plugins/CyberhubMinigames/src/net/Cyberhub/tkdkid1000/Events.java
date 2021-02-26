package net.Cyberhub.tkdkid1000;

import org.bukkit.configuration.file.FileConfiguration;

import net.Cyberhub.tkdkid1000.scripts.SurvivalSpawn;
import net.Cyberhub.tkdkid1000.scripts.VoidSpawn;

public class Events {

	private FileConfiguration config;
	private CyberhubMinigames cyberhubminigames;
	public Events(CyberhubMinigames cyberhubminigames, FileConfiguration config) {
		this.cyberhubminigames = cyberhubminigames;
		this.config = config;
	}
	public void register() {
		cyberhubminigames.getServer().getPluginManager().registerEvents(new VoidSpawn(config), cyberhubminigames);
		cyberhubminigames.getServer().getPluginManager().registerEvents(new SurvivalSpawn(config), cyberhubminigames);
	}
}
