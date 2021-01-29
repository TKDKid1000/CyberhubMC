package net.Cyberhub.tkdkid1000;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.Cyberhub.tkdkid1000.resources.Commands;
import net.Cyberhub.tkdkid1000.resources.Events;
import net.Cyberhub.tkdkid1000.resources.Gui;
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
	public static HashMap redteam = new HashMap();
	@SuppressWarnings("rawtypes")
	public static HashMap yellowteam = new HashMap();
	@SuppressWarnings("rawtypes")
	public static HashMap blueteam = new HashMap();
	@SuppressWarnings("rawtypes")
	public static HashMap greenteam = new HashMap();
	// players
	public static List<Player> redplayers = new ArrayList<Player>();
	public static List<Player> yellowplayers = new ArrayList<Player>();
	public static List<Player> blueplayers = new ArrayList<Player>();
	public static List<Player> greenplayers = new ArrayList<Player>();
	public static List<Player> players = new ArrayList<Player>();
	
	public YamlConfig gui = new YamlConfig(getDataFolder(), "gui");
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		gui.createConfig();
		new Events(this).register();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Gui(gui.getConfig()), this);
		getCommand("shopgui").setExecutor(new Gui(gui.getConfig()));
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
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		getLogger().info(ChatColor.RED + "Enabled: " + ChatColor.RESET + enabled);
		getLogger().info(ChatColor.RED + "Dead: " + ChatColor.RESET + deadteams);
		getLogger().info(ChatColor.RED + "Teams:");
		getLogger().info(ChatColor.RED + "YellowPlayers: " + ChatColor.RESET + yellowplayers);
		getLogger().info(ChatColor.RED + "YellowTeam: " + ChatColor.RESET + yellowteam);
		getLogger().info(ChatColor.RED + "RedPlayers: " + ChatColor.RESET + redplayers);
		getLogger().info(ChatColor.RED + "RedTeam: " + ChatColor.RESET + redteam);
		getLogger().info(ChatColor.RED + "bluePlayers: " + ChatColor.RESET + blueplayers);
		getLogger().info(ChatColor.RED + "blueTeam: " + ChatColor.RESET + blueteam);
		getLogger().info(ChatColor.RED + "greenPlayers: " + ChatColor.RESET + greenplayers);
		getLogger().info(ChatColor.RED + "greenTeam: " + ChatColor.RESET + greenteam);
		if (args.length == 1 && args[0].equalsIgnoreCase("block")) {
			getLogger().info(ChatColor.RED + "Blocks: " + ChatColor.RESET + blocks);
		}
		sender.sendMessage("check the console!");
		return true;
	}
}
