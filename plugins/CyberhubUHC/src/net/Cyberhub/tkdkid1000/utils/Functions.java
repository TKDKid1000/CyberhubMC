package net.Cyberhub.tkdkid1000.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Functions {

	public static void shrinkborder(int size) {
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			if (!(world.getName() == "hub" || world.getName().equalsIgnoreCase("hub"))) {
				WorldBorder border = world.getWorldBorder();
				border.setSize(border.getSize()-size);
			}
		}
	}
	
	public static void setBorder(int size) {
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			if (!(world.getName() == "hub" || world.getName().equalsIgnoreCase("hub"))) {
				WorldBorder border = world.getWorldBorder();
				border.setSize(size);
			}
		}
	}
	
	public static void damageBorder(double damage) {
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			if (!(world.getName() == "hub" || world.getName().equalsIgnoreCase("hub"))) {
				WorldBorder border = world.getWorldBorder();
				border.setDamageAmount(damage);
			}
		}
	}

	public static void tpall(Location loc) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(loc);
		}
	}
	
	public static List<Player> playersInWorld(World world) {
		List<Entity> entities = world.getEntities();
		List<Player> players = new ArrayList<Player>();
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				players.add(player);
			}
		}
		return players;
	}
	
	public static void tpr(World world, Player player) {
		int x = new Random().nextInt(5000);
		int z = new Random().nextInt(5000);
		Block block = world.getHighestBlockAt(x, z);
		player.teleport(block.getLocation());
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	public static void resetWorlds() {
		for (World world : Bukkit.getWorlds()) {
			if (!(world.getName() == "hub" || world.getName().equalsIgnoreCase("hub"))) {
				Bukkit.unloadWorld(world.getName(), false);
				Functions.deleteFolder(world.getWorldFolder());
			}
		}
		WorldCreator overworld = new WorldCreator("world");
		overworld.environment(Environment.NETHER);
		overworld.createWorld();
		WorldCreator nether = new WorldCreator("world_nether");
		nether.environment(Environment.THE_END);
		nether.createWorld();
		WorldCreator end = new WorldCreator("world_the_end");
		end.environment(Environment.NORMAL);
		end.createWorld();
	}
}
