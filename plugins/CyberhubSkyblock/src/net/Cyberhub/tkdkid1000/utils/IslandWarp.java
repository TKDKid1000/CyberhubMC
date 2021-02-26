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

import net.Cyberhub.tkdkid1000.CyberhubSkyblock;

public class IslandWarp implements CommandExecutor {

	HashMap<Player, Location> islands = CyberhubSkyblock.getMain().islands;
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Not enough args! Use the command like this:\n/islandwarp <player>");
			}
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				player.sendMessage(ChatColor.GREEN + "Warping to " + target.getName() + "'s island.");
				Location island = islands.get(target);
				if (island.getBlock().getType() != Material.AIR) {
					while (island.getBlock().getType() != Material.AIR) {
						island = island.add(0, 1, 0);
					}
				}
				player.teleport(island);
			}
		}
		return true;
	}

}
