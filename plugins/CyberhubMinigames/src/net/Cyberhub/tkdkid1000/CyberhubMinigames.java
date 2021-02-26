package net.Cyberhub.tkdkid1000;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.Cyberhub.tkdkid1000.games.BlockSumo;
import net.Cyberhub.tkdkid1000.games.Bridging;
import net.Cyberhub.tkdkid1000.utils.YamlConfig;

public class CyberhubMinigames extends JavaPlugin {

	public FileConfiguration config = getConfig();
	YamlConfig data = new YamlConfig(getDataFolder(), "data");
	@Override
	public void onEnable() {
		data.createConfig();
		new Events(this, config).register();
		new Bridging(this, config, data).setup();
		new BlockSumo(this, config).setup();
	}
	
	@Override
	public void onDisable() {
		
	}
}
