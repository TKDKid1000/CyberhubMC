package net.Cyberhub.tkdkid1000.resources;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class Generators {

	private FileConfiguration config;

	public Generators(FileConfiguration config) {
		this.config = config;
	}
	
	public void fillGen(String type, ItemStack item) {
		World map = Bukkit.getWorld(config.getString("map"));
		
		for (String gen : config.getStringList("gens."+type)) {
			String[] genloc = gen.split(",");
			map.dropItem(new Location(map, Integer.parseInt(genloc[0])+0.5, Integer.parseInt(genloc[1])+0.5, Integer.parseInt(genloc[2])+0.5), item);
		}
	}
}
