package net.Cyberhub.tkdkid1000;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.Cyberhub.tkdkid1000.utils.Functions;

public class GameBase implements Listener {

	public static List<Player> alive = new ArrayList<Player>();
	int timer = 0;
	private FileConfiguration config;
	boolean enabled = false;
	private GameBase instance;
	public GameBase(CyberhubUHC cyberhubUHC) {
		this.config = cyberhubUHC.config;
	}

	public GameBase instance() {
		return instance;
	}
	
	public void loop() {
		World hub = Bukkit.getWorld("hub");
		List<Player> players = Functions.playersInWorld(hub);
		if (players.size() >= config.getInt("startcount") && enabled != true) {
			enabled = true;
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("startmessage")));
			alive.clear();
			for (Player player : players) {
				alive.add(player);
				Functions.tpr(Bukkit.getWorld("world"), player);
			}
			players.clear();
			timer = 0;
		}
		if (enabled) {
			timer++;
			if (timer >= 20) {
				Functions.shrinkborder(3);
				timer = 0;
			}
			if (alive.size() < 2) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("winmessage")));
				alive.clear();
				for (World world : Bukkit.getWorlds()) {
					if (!(world.getName() == "hub" || world.getName().equalsIgnoreCase("hub"))) {
						for (Player player : world.getPlayers()) {
							player.teleport(new Location(Bukkit.getWorld("hub"), config.getDouble("spawnx"), config.getDouble("spawny"), config.getDouble("spawnz")));
							player.getInventory().clear();
						}
					}
				}
				Functions.resetWorlds();
				enabled = false;
			}
		}
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (alive.contains(player)) {
			alive.remove(player);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("deathmessage")));
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("deathalert")));
		}
	}
}