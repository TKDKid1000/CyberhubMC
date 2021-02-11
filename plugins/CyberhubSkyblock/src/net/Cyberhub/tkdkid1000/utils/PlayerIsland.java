package net.Cyberhub.tkdkid1000.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.Cyberhub.tkdkid1000.CyberhubSkyblock;
import net.Cyberhub.tkdkid1000.IslandReset;

public class PlayerIsland implements CommandExecutor {

	static HashMap<Player, Location> islands = CyberhubSkyblock.getMain().islands;
	public static String islandsToString(Location island) {
		if (island == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(island.getX()));
		sb.append(":");
		sb.append(String.valueOf(island.getY()));
		sb.append(":");
		sb.append(String.valueOf(island.getZ()));
		return sb.toString();
	}
	public static Location stringToislands(String island) {
		if (island == null || island.trim() == null) {
			return null;
		}
		String[] splitIsland = island.split(":");
		return new Location(Bukkit.getWorld(CyberhubSkyblock.getMain().getConfig().getString("world")), Double.parseDouble(splitIsland[0]), Double.parseDouble(splitIsland[1]), Double.parseDouble(splitIsland[2]));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (islands.containsKey(player)) {
				Location island = islands.get(player);
				if (island.getBlock().getType() != Material.AIR) {
					while (island.getBlock().getType() != Material.AIR) {
						island = island.add(0, 1, 0);
					}
				}
				player.teleport(island);
				player.sendMessage(ChatColor.GREEN + "Sending you to your island");
			} else {
				player.sendMessage(ChatColor.GREEN + "You don't currently own an island, creating one for you...");
				IslandReset.createMap(player);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.performCommand("island");
					}
				}.runTaskLater(CyberhubSkyblock.getMain().plugin, 40);
			}
		}
		return true;
	}
}
