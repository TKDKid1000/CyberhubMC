package net.Cyberhub.tkdkid1000;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.Cyberhub.tkdkid1000.utils.YamlConfig;

public class CyberhubUHC extends JavaPlugin implements Listener {

	int tick = 0;
	HashMap<Player, Integer> diamonds = new HashMap<Player, Integer>();
	public HashMap<Player, Integer> wins = new HashMap<Player, Integer>();
	private static CyberhubUHC instance;
	FileConfiguration config = getConfig();
	Plugin plugin = this;
	final GameBase base = new GameBase(this);
	public YamlConfig winsConfig = new YamlConfig(getDataFolder(), "wins");
	PluginLogger log = (PluginLogger) getLogger();
	@Override
	public void onEnable() {
		setup();
		winsConfig.createConfig();
		getServer().getPluginManager().registerEvents(new ChestDeath(), this);
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new GameBase(this), this);
		BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
        	@Override
        	public void run() {
        		base.loop();
        	}
        }, 0L, 1L);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static CyberhubUHC getMain() {
		return instance;
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.isOp() || player.hasPermission("cyberhubuhc.xrayimmune")) return;
		diamonds.putIfAbsent(player, 0);
		diamonds.replace(player, diamonds.get(player)+1);
	}
	
	public void setup() {
		if (Bukkit.getWorld("hub") == null) {
			log.info("Hub world does not exist. Creating one now.");
			WorldCreator hub = new WorldCreator("hub");
			hub.type(WorldType.FLAT);
			hub.createWorld();
		} else {
			log.info("Hub world already exists. Ignoring it.");
		}
		saveDefaultConfig();
		log.info("UHC is ready to go!");
	}
	
	public void loadData(Player player) {
		int playerwins = winsConfig.getConfig().getInt(player.getUniqueId() + ".wins");
		winsConfig.save();
		wins.put(player, playerwins);
	}
	
	public void saveData(Player player) {
		if (!wins.containsKey(player)) return;
		winsConfig.getConfig().set(player.getUniqueId() + ".wins", wins.get(player));
		winsConfig.save();
		wins.remove(player);
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
}
