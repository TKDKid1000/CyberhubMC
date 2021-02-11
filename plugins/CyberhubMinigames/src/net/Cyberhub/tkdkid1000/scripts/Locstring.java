package net.Cyberhub.tkdkid1000.scripts;

import org.bukkit.Location;
import org.bukkit.World;

public class Locstring {

	public static Location getLocationInt(String string, World world) {
		String[] stringarray = string.split(",");
		return new Location(world, Integer.parseInt(stringarray[0]), Integer.parseInt(stringarray[1]), Integer.parseInt(stringarray[2]));
 	}
	
	public static Location getLocationDouble(String string, World world) {
		String[] stringarray = string.split(",");
		return new Location(world, Double.parseDouble(stringarray[0]), Double.parseDouble(stringarray[1]), Double.parseDouble(stringarray[2]));
 	}
}
