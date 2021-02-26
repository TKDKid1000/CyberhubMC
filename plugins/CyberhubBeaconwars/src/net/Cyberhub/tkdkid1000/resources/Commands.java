package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor, TabExecutor {

	
	private CyberhubBeaconwars beacon;
	private FileConfiguration config;

	public Commands(CyberhubBeaconwars beacon, FileConfiguration config) {
		this.beacon = beacon;
		this.config = config;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
        	commands.add("setpos1");
        	commands.add("setpos2");
        	commands.add("setbeacon");
        	commands.add("setirongen");
        	commands.add("setgoldgen");
        	commands.add("setdiamondgen");
        	commands.add("setemeraldgen");
        	commands.add("setstargen");
        	commands.add("setbase");
        	commands.add("setmap");
        	commands.add("setrespawn");
        	commands.add("setirontime");
        	commands.add("setgoldtime");
        	commands.add("setdiamondtime");
        	commands.add("setemeraldtime");
        	commands.add("setstartime");
        	commands.add("setspawn");
        	commands.add("reload");
        	commands.add("help");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
        	if (args[0].equalsIgnoreCase("setbase") || args[0].equalsIgnoreCase("setislandgen") || args[0].equalsIgnoreCase("setbeacon")) {
        		for (String team : config.getStringList("teams")) {
        			commands.add(team);
        		}
        	} else if (args[0].equalsIgnoreCase("setrespawn")) {
        		commands.add("1");
        		commands.add("2");
        		commands.add("3");
        		commands.add("4");
        		commands.add("5");
        		commands.add("10");
        		commands.add("20");
        		commands.add("30");
        		commands.add("60");
        	} else if (args[0].contains("time")) {
        		commands.add("1");
        		commands.add("2");
        		commands.add("3");
        		commands.add("4");
        		commands.add("5");
        		commands.add("10");
        		commands.add("20");
        		commands.add("30");
        		commands.add("60");
        	}
        	StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			String locstring = x + "," + y + "," + z;
			if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
				String helpmsg = "&aCyberhub Beaconwars\n"
						+ "&6By https://github.com/TKDKid1000\n"
						+ "&aCommands\n"
						+ "&6All commands start with /beacon\n"
						+ "&6setpos1 | Sets position 1 of the arena bounding box\n"
						+ "&6setpos2 | Sets position 2 of the arena bounding box\n"
						+ "&6setbeacon <team> | Sets the beacon of a team. Do &4NOT&6 place a beacon here. It will auto place, and if you place one the game breaks\n"
						+ "&6setirongen | Sets an iron generator.\n"
						+ "&6setgoldgen | Sets a gold generator.\n"
						+ "&6setdiamondgen | Sets a diamond generator.\n"
						+ "&6setemeraldgen | Sets an emerald generator.\n"
						+ "&6setstargen | Sets a star generator.\n"
						+ "&6setbase <team> | Sets a teams respawn point.\n"
						+ "&6setmap | Sets the map. Run this command in the world you want the map to be.\n"
						+ "&6setrespawn <seconds> | Sets the respawn time after death.\n"
						+ "&6setirontime <seconds> | Sets the interval that iron spawns.\n"
						+ "&6setgoldtime <seconds> | Sets the interval that gold spawns.\n"
						+ "&6setdiamondtime <seconds> | Sets the interval that diamond spawns.\n"
						+ "&6setemeraldtime <seconds> | Sets the interval that emerald spawns.\n"
						+ "&6setstartime <seconds> | Sets the interval that star spawns.\n"
						+ "&6setspawn | Sets the location that players get teleported to on join. (waiting area)\n"
						+ "&6reload | Reloads the beaconwars config. Do this after modifying anything to apply those changes.\n"
						+ "&6help | Shows this message";
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', helpmsg));
			} else if (args[0].equalsIgnoreCase("setpos1")) {
				config.set("boundingbox.x1", x);
				config.set("boundingbox.y1", y);
				config.set("boundingbox.z1", z);
				beacon.saveConfig();
				player.sendMessage(ChatColor.GREEN + "Set the boundingbox 1 to " + locstring);
			} else if (args[0].equalsIgnoreCase("setpos2")) {
				config.set("boundingbox.x2", x);
				config.set("boundingbox.y2", y);
				config.set("boundingbox.z2", z);
				beacon.saveConfig();
				player.sendMessage(ChatColor.GREEN + "Set the boundingbox 2 to " + locstring);
			} else if (args[0].equalsIgnoreCase("setbeacon")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a team!");
				} else {
					if (config.getStringList("teams").contains(args[1])) {
						config.set("beacons." + args[1], locstring);
						beacon.saveConfig();
						player.sendMessage(ChatColor.GREEN + "Set " + args[1] + " teams beacon to " + locstring);
					} else {
						player.sendMessage(ChatColor.RED + "That isn't a valid team!");
					}
				}
			} else if (args[0].equalsIgnoreCase("setbase")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a team!");
				} else {
					if (config.getStringList("teams").contains(args[1])) {
						config.set("bases." + args[1], locstring);
						beacon.saveConfig();
						player.sendMessage(ChatColor.GREEN + "Set " + args[1] + " teams respawn point to " + locstring);
					} else {
						player.sendMessage(ChatColor.RED + "That isn't a valid team!");
					}
				}
			} else if (args[0].equalsIgnoreCase("setmap")) {
				config.set("map", loc.getWorld().getName());
				beacon.saveConfig();
				player.sendMessage(ChatColor.GREEN + "Set the map to " + loc.getWorld().getName());
			} else if (args[0].equalsIgnoreCase("setrespawn")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a respawn time. (seconds)");
				} else {
					config.set("respawntime", Integer.parseInt(args[1]));
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set the respawn time to " + args[1] + " seconds.");
				}			
			} else if (args[0].equalsIgnoreCase("setirongen")) {
				List<String> gens = config.getStringList("gens.iron");
				if (gens.contains(locstring)) {
					gens.remove(locstring);
					config.set("gens.iron", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Removed the currently set iron gen");
				} else {
					gens.add(locstring);
					config.set("gens.iron", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set a iron gen at your current location");
				}
			} else if (args[0].equalsIgnoreCase("setgoldgen")) {
				List<String> gens = config.getStringList("gens.gold");
				if (gens.contains(locstring)) {
					gens.remove(locstring);
					config.set("gens.gold", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Removed the currently set gold gen");
				} else {
					gens.add(locstring);
					config.set("gens.gold", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set a gold gen at your current location");
				}
			} else if (args[0].equalsIgnoreCase("setdiamondgen")) {
				List<String> gens = config.getStringList("gens.diamond");
				if (gens.contains(locstring)) {
					gens.remove(locstring);
					config.set("gens.diamond", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Removed the currently set diamond gen");
				} else {
					gens.add(locstring);
					config.set("gens.diamond", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set a diamond gen at your current location");
				}
			} else if (args[0].equalsIgnoreCase("setemeraldgen")) {
				List<String> gens = config.getStringList("gens.emerald");
				if (gens.contains(locstring)) {
					gens.remove(locstring);
					config.set("gens.emerald", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Removed the currently set emerald gen");
				} else {
					gens.add(locstring);
					config.set("gens.emerald", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set a emerald gen at your current location");
				}
			} else if (args[0].equalsIgnoreCase("setstargen")) {
				List<String> gens = config.getStringList("gens.star");
				if (gens.contains(locstring)) {
					gens.remove(locstring);
					config.set("gens.star", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Removed the currently set star gen");
				} else {
					gens.add(locstring);
					config.set("gens.star", gens);
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set a star gen at your current location");
				}
			} else if (args[0].equalsIgnoreCase("setirontime")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a time! (seconds)");
				} else {
					config.set("gentime.iron", Integer.parseInt(args[1]));
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set iron gen time to " + args[1]);
				}
			} else if (args[0].equalsIgnoreCase("setgoldtime")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a time! (seconds)");
				} else {
					config.set("gentime.gold", Integer.parseInt(args[1]));
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set gold gen time to " + args[1]);
				}
			} else if (args[0].equalsIgnoreCase("setdiamondtime")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a time! (seconds)");
				} else {
					config.set("gentime.diamond", Integer.parseInt(args[1]));
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set diamond gen time to " + args[1]);
				}
			} else if (args[0].equalsIgnoreCase("setemeraldtime")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a time! (seconds)");
				} else {
					config.set("gentime.emerald", Integer.parseInt(args[1]));
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set emerald gen time to " + args[1]);
				}
			} else if (args[0].equalsIgnoreCase("setstartime")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.RED + "Please specify a time! (seconds)");
				} else {
					config.set("gentime.star", Integer.parseInt(args[1]));
					beacon.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set star gen time to " + args[1]);
				}
			} else if (args[0].equalsIgnoreCase("setspawn")) {
				config.set("spawn", locstring);
				beacon.saveConfig();
				player.sendMessage(ChatColor.GREEN + "Set the spawn (waiting area) to " + locstring);
			} else if (args[0].equalsIgnoreCase("reload")) {
				beacon.reloadConfig();
				beacon.gui.reload();
				player.sendMessage(ChatColor.GREEN + "Reloading the beaconwars config. This may lag for a moment.");
			} else if (args[0].equalsIgnoreCase("end")) {
				if (CyberhubBeaconwars.enabled) {
					player.sendMessage(ChatColor.GREEN + "Ending the active beaconwars game...");
					for (int i=1; i<8; i++) {
						CyberhubBeaconwars.playerlist.get(i).clear();
					}
					CyberhubBeaconwars.deadteams = 7;
				} else {
					player.sendMessage(ChatColor.RED + "There isn't a game running right now.");
				}
			} else if (args[0].equalsIgnoreCase("start")) {
				if (!CyberhubBeaconwars.enabled) {
					Location playerloc = player.getLocation();
					playerloc.setWorld(Bukkit.getWorld("world"));
					Location maploc = player.getLocation();
					playerloc.setWorld(Bukkit.getWorld(config.getString("map")));
					player.teleport(playerloc);
					player.teleport(maploc);
					player.sendMessage(ChatColor.GREEN + "Starting a new beaconwars game...");
				} else {
					player.sendMessage(ChatColor.RED + "There is a game running right now.");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
		}
		return true;
	}

}
