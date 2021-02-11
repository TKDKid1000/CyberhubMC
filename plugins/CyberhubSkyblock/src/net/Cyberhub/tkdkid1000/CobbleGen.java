package net.Cyberhub.tkdkid1000;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;

import net.Cyberhub.tkdkid1000.utils.AntiAfk;

public class CobbleGen implements Listener {

	FileConfiguration config = CyberhubSkyblock.getMain().config;
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCobbleStone(BlockFromToEvent e) {
		int id = e.getBlock().getTypeId();
		if (id >= 8 && id <= 11) {
			Block block = e.getToBlock();
			int toid = block.getTypeId();
			if (toid == 0) {
				if (generatesCobble(id, block)) {
					int choice = new Random().nextInt(config.getInt("genblocks.max"));
					int coal = config.getInt("genblocks.coal");
					int iron = config.getInt("genblocks.iron");
					int gold = config.getInt("genblocks.gold");
					int redstone = config.getInt("genblocks.redstone");
					int lapis = config.getInt("genblocks.lapis");
					int emerald = config.getInt("genblocks.emerald");
					int diamond = config.getInt("genblocks.diamond");
					int cobblestone = config.getInt("genblocks.cobblestone");
					if (choice >= coal && choice < iron) {
						block.setType(Material.COAL_ORE);
					}
					else if (choice >= iron && choice < gold) {
						block.setType(Material.IRON_ORE);
					}
					else if (choice >= gold && choice < redstone) {
						block.setType(Material.GOLD_ORE);
					}
					else if (choice >= redstone && choice < lapis) {
						block.setType(Material.REDSTONE_ORE);
					}
					else if (choice >= lapis && choice < emerald) {
						block.setType(Material.LAPIS_ORE);
					}
					else if (choice >= emerald && choice < diamond) {
						block.setType(Material.EMERALD_ORE);
					}
					else if (choice >= diamond && choice < cobblestone) {
						block.setType(Material.DIAMOND_ORE);
					}
					else {
						int afkchoice = new Random().nextInt(5);
						if (afkchoice == 3) {
							block.setType(Material.CHEST);
							Chest chest = (Chest) block.getState();
							chest.getBlockInventory().addItem(new ItemStack(Material.DIAMOND));
							AntiAfk.chests.add(block);
						} else {
							block.setType(Material.COBBLESTONE);
						}
					}
				}
			}
		}
	}
	
	// if the block can generate cobblestone
	
	private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	@SuppressWarnings("deprecation")
	public boolean generatesCobble(int id, Block block) {
		int mirrorID1 = (id == 8 || id == 9 ? 10 : 8);
		int mirrorID2 = (id == 8 || id == 9 ? 11 : 9);
		for (BlockFace face : faces) {
			Block relative = block.getRelative(face, 1);
			if (relative.getTypeId() == mirrorID1 || relative.getTypeId() == mirrorID2) {
				return true;
			}
		}
		return false;
	}
}
