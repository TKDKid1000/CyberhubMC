package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
	
	public static String getTeam(Player player) {
		if (CyberhubBeaconwars.blueplayers.contains(player)) return "b";
		if (CyberhubBeaconwars.yellowplayers.contains(player)) return "y";
		if (CyberhubBeaconwars.redplayers.contains(player)) return "r";
		if (CyberhubBeaconwars.greenplayers.contains(player)) return "g";
		return "n";
	}
}
