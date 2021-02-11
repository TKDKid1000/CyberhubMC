package net.Cyberhub.tkdkid1000;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.Cyberhub.tkdkid1000.utils.AntiAfk;
import net.Cyberhub.tkdkid1000.utils.IslandWarp;
import net.Cyberhub.tkdkid1000.utils.LeaveJoinSaving;
import net.Cyberhub.tkdkid1000.utils.PlayerIsland;
import net.Cyberhub.tkdkid1000.utils.YamlConfig;

public class CyberhubSkyblock extends JavaPlugin {

	public HashMap<Player, Location> islands = new HashMap<Player, Location>();
	public HashMap<Player, Double> money = new HashMap<Player, Double>();
	public HashMap<String, Double> worth = new HashMap<String, Double>();
	FileConfiguration config = getConfig();
	Logger log = getLogger();
	public Plugin plugin = this;
	private static CyberhubSkyblock instance;
	public YamlConfig islandsConfig = new YamlConfig(getDataFolder(), "islands");
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		islandsConfig.createConfig();
		this.getCommand("island").setExecutor(new PlayerIsland());
		this.getCommand("islandwarp").setExecutor(new IslandWarp());
		getServer().getPluginManager().registerEvents(new LeaveJoinSaving(), this);
		getServer().getPluginManager().registerEvents(new CobbleGen(), this);
		getServer().getPluginManager().registerEvents(new AntiAfk(), this);
		if (Bukkit.getWorld(config.getString("world")) != null) {
			log.info("Skyblock world already exists, not creating new one");
		} else {
			log.info("Creating new blank world named " + config.getString("world"));
			WorldCreator wc = new WorldCreator(config.getString("world"));
			wc.type(WorldType.FLAT);
			wc.generatorSettings("2;0;1;");
			wc.createWorld();
		}
	}
	@Override
	public void onDisable() {
		
	}
	
	public static CyberhubSkyblock getMain() {
		return instance;
	}
	public void loadIslands(Player player) {
		String iString = islandsConfig.getConfig().getString(player.getUniqueId() + ".island");
		if (iString == null) {
			return;
		}
		islandsConfig.save();
		islands.put(player, PlayerIsland.stringToislands(iString));
	}
	
	public void saveIslands(Player player) {
		if (!islands.containsKey(player)) {
			return;
		}
		islandsConfig.getConfig().set(player.getUniqueId() + ".island", PlayerIsland.islandsToString(islands.get(player)));
		islandsConfig.save();
		islands.remove(player);
	}
}
