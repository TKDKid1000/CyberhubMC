package net.Cyberhub.tkdkid1000;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.Cyberhub.tkdkid1000.resources.Commands;
import net.Cyberhub.tkdkid1000.resources.Enchants;
import net.Cyberhub.tkdkid1000.resources.Events;
import net.Cyberhub.tkdkid1000.resources.Gui;
import net.Cyberhub.tkdkid1000.resources.HotbarItems;
import net.Cyberhub.tkdkid1000.resources.StatGUI;
import net.Cyberhub.tkdkid1000.resources.VoidSpawn;
import net.Cyberhub.tkdkid1000.utils.YamlConfig;
import net.md_5.bungee.api.ChatColor;

public class CyberhubBeaconwars extends JavaPlugin implements Listener {

	public static boolean enabled = false;
	public static int deadteams;
	public static List<Block> blocks;
	public Plugin plugin = this;
	private static CyberhubBeaconwars instance;
	public FileConfiguration config = getConfig();
	// player teams
	@SuppressWarnings("rawtypes")
	public static List<HashMap> teamlist = new ArrayList<HashMap>();
	// players
	public static List<List<Player>> playerlist = new ArrayList<List<Player>>();
	
	public static List<String> colors = new ArrayList<String>();
	public static List<Player> players;
	
	public YamlConfig gui = new YamlConfig(getDataFolder(), "gui");
	public YamlConfig enchants = new YamlConfig(getDataFolder(), "enchants");
	public YamlConfig playerdata = new YamlConfig(getDataFolder(), "playerdata");
	@Override
	public void onEnable() {
		saveDefaultConfig();
		gui.createConfig();
		enchants.createConfig();
		playerdata.createConfig();
		new Events(this).register();
		new StatGUI(this).setup();
		new HotbarItems(this).setup();
		new VoidSpawn(this, config).register();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Gui(gui.getConfig()), this);
		getCommand("shopgui").setExecutor(new Gui(gui.getConfig()));
		getServer().getPluginManager().registerEvents(new Enchants(enchants.getConfig()), this);
		getCommand("enchgui").setExecutor(new Enchants(enchants.getConfig()));
		getCommand("beacon").setExecutor(new Commands(this, config));
		getCommand("beacon").setTabCompleter(new Commands(this, config));
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static CyberhubBeaconwars getInstance() {
		return instance;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if (!enabled) {
			String[] locstring = config.getString("spawn").split(",");
			if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("map"))) {
				event.getPlayer().teleport(new Location(Bukkit.getWorld(config.getString("map")), Integer.parseInt(locstring[0]), Integer.parseInt(locstring[1]), Integer.parseInt(locstring[2])));
				if (event.getPlayer().getWorld().getPlayers().size() == config.getInt("minplayers")) {
					for (Player p : event.getPlayer().getWorld().getPlayers()) {
						p.sendMessage(ChatColor.GOLD + "Game starts in 10 seconds.");
					}
					int[] loop = {5,4,3,2,1};
					final Game game = new Game(this, config);
					for (int x : loop) {
						new BukkitRunnable() {

							@Override
							public void run() {
								for (Player p : event.getPlayer().getWorld().getPlayers()) {
									p.sendMessage(ChatColor.GOLD + "Game starts in " + (6-x) + " seconds.");
								}
							}
							
						}.runTaskLater(plugin, x*20);
					}
					new BukkitRunnable() {

						@Override
						public void run() {
							for (Player p : event.getPlayer().getWorld().getPlayers()) {
								p.sendMessage(ChatColor.GOLD + "Game started!");
							}
							game.start();
						}
						
					}.runTaskLater(plugin, 5*20);
				}
			}
		} else {
			if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("map"))) {
				event.getPlayer().sendMessage(ChatColor.GREEN + "You are spectating.");
			}
		}
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("spawnworld"))) {
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		getLogger().info(ChatColor.RED + "Enabled: " + ChatColor.RESET + enabled);
		getLogger().info(ChatColor.RED + "Dead: " + ChatColor.RESET + deadteams);
		if (args.length == 1 && args[0].equalsIgnoreCase("block")) {
			getLogger().info(ChatColor.RED + "Blocks: " + ChatColor.RESET + blocks);
		}
		sender.sendMessage("check the console!");
		return true;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (playerdata.getConfig().getString("playerdata."+player.getUniqueId().toString()+".wins") == null) {
			playerdata.getConfig().set("playerdata."+player.getUniqueId()+".wins", 0);
			playerdata.save();
		}
		if (playerdata.getConfig().getString("playerdata."+player.getUniqueId().toString()+".kills") == null) {
			playerdata.getConfig().set("playerdata."+player.getUniqueId()+".kills", 0);
			playerdata.save();
		}
		if (playerdata.getConfig().getString("playerdata."+player.getUniqueId().toString()+".deaths") == null) {
			playerdata.getConfig().set("playerdata."+player.getUniqueId()+".deaths", 0);
			playerdata.save();
		}
	}
}
