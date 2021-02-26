package net.Cyberhub.tkdkid1000.customs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class GoldenTrees extends Modifier {

	boolean enabled = false;
	List<Material> leaves = new ArrayList<Material>();
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		leaves.add(Material.OAK_LEAVES);
		leaves.add(Material.SPRUCE_LEAVES);
		leaves.add(Material.BIRCH_LEAVES);
		leaves.add(Material.JUNGLE_LEAVES);
		leaves.add(Material.ACACIA_LEAVES);
		leaves.add(Material.DARK_OAK_LEAVES);
		if (leaves.contains(event.getBlock().getType())) {
			Random random = new Random();
			if (random.nextInt(10) == 5) {
				Location loc = event.getBlock().getLocation();
				loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLDEN_APPLE));
			}
		}
	}
}
