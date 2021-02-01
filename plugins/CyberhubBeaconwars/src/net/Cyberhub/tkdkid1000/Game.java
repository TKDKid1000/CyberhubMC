package net.Cyberhub.tkdkid1000;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import net.Cyberhub.tkdkid1000.resources.Functions;
import net.Cyberhub.tkdkid1000.resources.Generators;
import net.Cyberhub.tkdkid1000.utils.BoundingBox;
import net.md_5.bungee.api.ChatColor;

public class Game {

	private CyberhubBeaconwars beaconwars;
	private FileConfiguration config;
	
	static int irongen = 0;
	static int goldgen = 0;
	static int diamondgen = 0;
	static int emeraldgen = 0;
	static int stargen = 0;
	/*
	private static int xpos;
	private static int ypos;
	private static int zpos;
	*/
	public Game(CyberhubBeaconwars beaconwars, FileConfiguration config) {
		this.beaconwars = beaconwars;
		this.config = config;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void init() {
		World map = Bukkit.getWorld(config.getString("map"));
		for (Entity ent : map.getEntities()) {
			if (ent instanceof Item) {
				ent.remove();
			}
		}
		for (Player player : map.getPlayers()) {
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
		}
		CyberhubBeaconwars.deadteams = 0;
		// clear teams
		CyberhubBeaconwars.playerlist.clear();
		CyberhubBeaconwars.teamlist.clear();
		// set stuff
		CyberhubBeaconwars.colors = config.getStringList("teams");
		for (int x=0; x<8; x++) {
			HashMap team = new HashMap();
			String color = CyberhubBeaconwars.colors.get(x);
			team.put("base", new Location(map,
					Double.parseDouble(config.getString("bases."+color).split(",")[0]),
					Double.parseDouble(config.getString("bases."+color).split(",")[1]),
					Double.parseDouble(config.getString("bases."+color).split(",")[2])));
			team.put("beacon", new Location(map,
					Double.parseDouble(config.getString("beacons."+color).split(",")[0]),
					Double.parseDouble(config.getString("beacons."+color).split(",")[1]),
					Double.parseDouble(config.getString("beacons."+color).split(",")[2])));
			team.put("beaconalive", true);
			((Location) team.get("beacon")).getBlock().setType(Material.AIR);
			System.out.println(team);
			CyberhubBeaconwars.teamlist.add(team);
			CyberhubBeaconwars.playerlist.add(new ArrayList<Player>());
		}
		CyberhubBeaconwars.blocks = Functions.getBlocks(new BoundingBox(config.getInt("boundingbox.x1"), 
				config.getInt("boundingbox.y1"), 
				config.getInt("boundingbox.z1"), 
				config.getInt("boundingbox.x2"), 
				config.getInt("boundingbox.y2"), 
				config.getInt("boundingbox.z2")), map);
		for (int x=0; x<8; x++) {
			HashMap team = CyberhubBeaconwars.teamlist.get(x);
			((Location) team.get("beacon")).getBlock().setType(Material.BEACON);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void start() {
		if (CyberhubBeaconwars.enabled) return;
		init();
		World map = Bukkit.getWorld(config.getString("map"));
		if (map.getPlayers().size() >= config.getInt("minplayers")) {
			CyberhubBeaconwars.enabled = true;
			List<Player> players = map.getPlayers();
			Collections.shuffle(players);
			List<List<Player>> teams;
			teams = Lists.partition(players, 1);
			System.out.println(teams);
			for (int x=0; x<teams.size(); x++) {
				if (teams.get(x) != null) {
					System.out.println(x);
					CyberhubBeaconwars.playerlist.get(x).addAll(teams.get(x));
				}
			}
			CyberhubBeaconwars.players = players;
			CyberhubBeaconwars.deadteams = 8-teams.size();
			for (Player p : CyberhubBeaconwars.players) {
				p.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
				p.setHealth(20.0);
				p.setFoodLevel(20);
				for (int x=0; x<8; x++) {
					HashMap team = CyberhubBeaconwars.teamlist.get(x);
					if (CyberhubBeaconwars.playerlist.get(x).contains(p)) {
						p.teleport((Location) team.get("base"));
					}
				}
			}
			new BukkitRunnable() {

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					for (Player p : CyberhubBeaconwars.players) {
						if (p.isDead()) {
							p.spigot().respawn();
						}
					}
					irongen++;
					goldgen++;
					diamondgen++;
					emeraldgen++;
					stargen++;
					if (irongen >= config.getInt("gentime.iron")*20) {
						irongen=0;
						new Generators(config).fillGen("iron", new ItemStack(Material.IRON_INGOT));
					}
					if (goldgen >= config.getInt("gentime.gold")*20) {
						goldgen=0;
						new Generators(config).fillGen("gold", new ItemStack(Material.GOLD_INGOT));
					}
					if (diamondgen >= config.getInt("gentime.diamond")*20) {
						diamondgen=0;
						new Generators(config).fillGen("diamond", new ItemStack(Material.DIAMOND));
					}
					if (emeraldgen >= config.getInt("gentime.emerald")*20) {
						emeraldgen=0;
						new Generators(config).fillGen("emerald", new ItemStack(Material.EMERALD));
					}
					if (stargen >= config.getInt("gentime.star")*20) {
						stargen=0;
						new Generators(config).fillGen("star", new ItemStack(Material.NETHER_STAR));
					}
					if (CyberhubBeaconwars.deadteams >= 7) {
						for (int i=0; i<8; i++) {
							List<Player> team = CyberhubBeaconwars.playerlist.get(i);
							if (team.size() != 0) {
								for (Player p : team) {
									p.sendMessage(ChatColor.GREEN + "You won!");
									try {
										Economy.add(p.getName(), 100);
										p.sendMessage(ChatColor.GOLD + "100 Gold, Game won.");
									} catch (NoLoanPermittedException | UserDoesNotExistException e) {
										p.sendMessage(ChatColor.RED + "Failed to give you your gold. Please contact a server admin.");
										e.printStackTrace();
									}
								}
								Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + CyberhubBeaconwars.colors.get(i) + " team won the game!");
							}
						}
						for (Player p : map.getPlayers()) {
							p.getInventory().clear();
							p.performCommand("spawn");
							p.setGameMode(GameMode.SURVIVAL);
						}
						CyberhubBeaconwars.enabled = false;
						new BukkitRunnable() {

							@Override
							public void run() {
								/*
								BoundingBox box = new BoundingBox(config.getInt("boundingbox.x1"), 
										config.getInt("boundingbox.y1"), 
										config.getInt("boundingbox.z1"), 
										config.getInt("boundingbox.x2"), 
										config.getInt("boundingbox.y2"), 
										config.getInt("boundingbox.z2"));
								
								int x1 = (int) box.getMinX();
								int y1 = (int) box.getMinY();
								int z1 = (int) box.getMinZ();
								int x2 = (int) box.getMaxX();
								int y2 = (int) box.getMaxY();
								int z2 = (int) box.getMaxZ();
								*/
								Clipboard clipboard;
								File schem = new File("plugins"+File.separator+"WorldEdit"+File.separator+"schematics"+File.separator+config.getString("map")+".schematic");
								ClipboardFormat format = ClipboardFormat.findByFile(schem);
								try {
									ClipboardReader reader = format.getReader(new FileInputStream(schem));
									clipboard = reader.read(LegacyWorldData.getInstance());
									EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitUtil.getLocalWorld(map), -1);
									Operation operation = new ClipboardHolder(clipboard, LegacyWorldData.getInstance())
								            .createPaste(editSession, LegacyWorldData.getInstance())
								            .to(new Vector(9921, 119, 100252))
								            .build();
									try {
										Operations.complete(operation);
										System.out.println("done!");
									} catch (WorldEditException e) {
										e.printStackTrace();
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							
						}.runTaskLater(beaconwars, 100);
						this.cancel();
					}
				}
				
			}.runTaskTimer(beaconwars.plugin, 1, 1);
		}
	}
}
