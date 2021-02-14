package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;
import net.Cyberhub.tkdkid1000.utils.BoundingBox;

public class Functions {

	public static List<Block> getBlocks(BoundingBox box, World world) {
		int x1 = (int) box.getMinX();
		int y1 = (int) box.getMinY();
		int z1 = (int) box.getMinZ();
		int x2 = (int) box.getMaxX();
		int y2 = (int) box.getMaxY();
		int z2 = (int) box.getMaxZ();
		List<Block> blocks = new ArrayList<Block>();
		for (int x=x1; x<x2+1; x++) {
			for (int y=y1; y<y2+1; y++) {
				for (int z=z1; z<z2+1; z++) {
					if (!(new Location(world, x, y, z).getBlock().getType() == Material.AIR)) {
						blocks.add(new Location(world, x, y, z).getBlock());
					}
				}
			}
		}
		return blocks;
	}
	
	public static void elimPlayer(Player player) {
		for (UUID uuid : CyberhubBeaconwars.players) {
			Player p = Bukkit.getPlayer(uuid);
			p.sendMessage(ChatColor.RED + player.getName() + " has been eliminated!");
		}
		player.sendMessage(ChatColor.RED + "You have been eliminated from the game!");
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(new ItemStack[] {new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR)});
		for (int x=0; x<8; x++) {
			List<UUID> players = CyberhubBeaconwars.playerlist.get(x);
			List<String> colors = CyberhubBeaconwars.colors;
			if (players.contains(player.getUniqueId())) {
				players.remove(player.getUniqueId());
				if (players.size() == 0) {
					CyberhubBeaconwars.deadteams++;
					for (UUID uuid: CyberhubBeaconwars.players) {
						Player p = Bukkit.getPlayer(uuid);
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + colors.get(x) + " team has been eliminated!");
					}
				}
			}
			if (CyberhubBeaconwars.players.contains(player.getUniqueId())) {
				CyberhubBeaconwars.players.remove(player.getUniqueId());
			}
		}
	}
}
