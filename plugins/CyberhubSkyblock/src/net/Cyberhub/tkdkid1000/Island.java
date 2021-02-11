package net.Cyberhub.tkdkid1000;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class Island {


	private Location loc;
	public Island(Location loc) {
		this.loc = loc;
	}
	
	@SuppressWarnings("deprecation")
	public void build(Material type, int xm, int ym, int zm, Material top, TreeType tree) {
		for (int x=0; x<xm; x++) {
			for (int y=0; y<ym; y++) {
				for (int z=0; z<zm; z++) {
					loc.clone().add(x, y, z).getBlock().setType(type);
				}
			}
		}
		for (int x=0; x<xm; x++) {
			for (int z=0; z<zm; z++) {
				loc.clone().add(x, ym-1, z).getBlock().setType(top);
			}
		}
		World world = Bukkit.getWorld(CyberhubSkyblock.getMain().config.getString("world"));
		world.generateTree(loc.clone().add(xm/2, ym, 0), tree);
		loc.clone().add((xm/2)+1, ym, 0).getBlock().setType(Material.CHEST);
		Chest chest = (Chest) loc.clone().add((xm/2)+1, ym, 0).getBlock().getState();
		for (String s : CyberhubSkyblock.getMain().config.getStringList("items")) {
			String[] item = s.split(" ");
			chest.getBlockInventory().addItem(new ItemStack(Material.getMaterial(Integer.parseInt(item[0])), Integer.parseInt(item[1])));
		}
	}
}
