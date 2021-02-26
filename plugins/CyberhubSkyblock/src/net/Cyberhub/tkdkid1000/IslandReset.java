package net.Cyberhub.tkdkid1000;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class IslandReset {
	static HashMap<Player, Location> islands = CyberhubSkyblock.getMain().islands;
	static FileConfiguration config = CyberhubSkyblock.getMain().getConfig();
	public static void createMap(Player player) {
		Location island = new Location(Bukkit.getWorld(config.getString("world")), islands.size()*config.getInt("seperate"), config.getInt("height"), islands.size()*config.getInt("seperate"));
		Island i = new Island(island);
		i.build(Material.DIRT, 9, 4, 3, Material.GRASS, TreeType.TREE);
		islands.put(player, island);
	}
}
