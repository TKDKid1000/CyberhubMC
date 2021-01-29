package net.Cyberhub.tkdkid1000;

import java.util.ArrayList;
import java.util.Collections;
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
	
	public Game(CyberhubBeaconwars beaconwars, FileConfiguration config) {
		this.beaconwars = beaconwars;
		this.config = config;
	}
	
	@SuppressWarnings("unchecked")
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
		CyberhubBeaconwars.blocks = Functions.getBlocks(new BoundingBox(config.getInt("boundingbox.x1"), 
				config.getInt("boundingbox.y1"), 
				config.getInt("boundingbox.z1"), 
				config.getInt("boundingbox.x2"), 
				config.getInt("boundingbox.y2"), 
				config.getInt("boundingbox.z2")), map);
		// clear teams
		CyberhubBeaconwars.redteam.clear();
		CyberhubBeaconwars.greenteam.clear();
		CyberhubBeaconwars.blueteam.clear();
		CyberhubBeaconwars.yellowteam.clear();
		// clear players
		CyberhubBeaconwars.redplayers.clear();
		CyberhubBeaconwars.greenplayers.clear();
		CyberhubBeaconwars.blueplayers.clear();
		CyberhubBeaconwars.yellowplayers.clear();
		// set stuff
		//red
		CyberhubBeaconwars.redteam.put("base", new Location(map,
				Double.parseDouble(config.getString("bases.red").split(",")[0]),
				Double.parseDouble(config.getString("bases.red").split(",")[1]),
				Double.parseDouble(config.getString("bases.red").split(",")[2])));
		CyberhubBeaconwars.redteam.put("beacon", new Location(map,
				Double.parseDouble(config.getString("beacons.red").split(",")[0]),
				Double.parseDouble(config.getString("beacons.red").split(",")[1]),
				Double.parseDouble(config.getString("beacons.red").split(",")[2])));
		CyberhubBeaconwars.redteam.put("beaconalive", true);
		CyberhubBeaconwars.redteam.put("genlevel", 1);
		//green
		CyberhubBeaconwars.greenteam.put("base", new Location(map,
				Double.parseDouble(config.getString("bases.green").split(",")[0]),
				Double.parseDouble(config.getString("bases.green").split(",")[1]),
				Double.parseDouble(config.getString("bases.green").split(",")[2])));
		CyberhubBeaconwars.greenteam.put("beacon", new Location(map,
				Double.parseDouble(config.getString("beacons.green").split(",")[0]),
				Double.parseDouble(config.getString("beacons.green").split(",")[1]),
				Double.parseDouble(config.getString("beacons.green").split(",")[2])));
		CyberhubBeaconwars.greenteam.put("beaconalive", true);
		CyberhubBeaconwars.greenteam.put("genlevel", 1);
		//blue
		CyberhubBeaconwars.blueteam.put("base", new Location(map,
				Double.parseDouble(config.getString("bases.blue").split(",")[0]),
				Double.parseDouble(config.getString("bases.blue").split(",")[1]),
				Double.parseDouble(config.getString("bases.blue").split(",")[2])));
		CyberhubBeaconwars.blueteam.put("beacon", new Location(map,
				Double.parseDouble(config.getString("beacons.blue").split(",")[0]),
				Double.parseDouble(config.getString("beacons.blue").split(",")[1]),
				Double.parseDouble(config.getString("beacons.blue").split(",")[2])));
		CyberhubBeaconwars.blueteam.put("beaconalive", true);
		CyberhubBeaconwars.blueteam.put("genlevel", 1);
		//yellow
		CyberhubBeaconwars.yellowteam.put("base", new Location(map,
				Double.parseDouble(config.getString("bases.yellow").split(",")[0]),
				Double.parseDouble(config.getString("bases.yellow").split(",")[1]),
				Double.parseDouble(config.getString("bases.yellow").split(",")[2])));
		CyberhubBeaconwars.yellowteam.put("beacon", new Location(map,
				Double.parseDouble(config.getString("beacons.yellow").split(",")[0]),
				Double.parseDouble(config.getString("beacons.yellow").split(",")[1]),
				Double.parseDouble(config.getString("beacons.yellow").split(",")[2])));
		CyberhubBeaconwars.yellowteam.put("beaconalive", true);
		CyberhubBeaconwars.yellowteam.put("genlevel", 1);
		// place beacons
		((Location) CyberhubBeaconwars.redteam.get("beacon")).getBlock().setType(Material.BEACON);
		((Location) CyberhubBeaconwars.greenteam.get("beacon")).getBlock().setType(Material.BEACON);
		((Location) CyberhubBeaconwars.blueteam.get("beacon")).getBlock().setType(Material.BEACON);
		((Location) CyberhubBeaconwars.yellowteam.get("beacon")).getBlock().setType(Material.BEACON);
	}
	
	public void start() {
		if (CyberhubBeaconwars.enabled) return;
		init();
		World map = Bukkit.getWorld(config.getString("map"));
		if (map.getPlayers().size() >= config.getInt("minplayers")) {
			CyberhubBeaconwars.enabled = true;
			List<Player> players = map.getPlayers();
			Collections.shuffle(players);
			List<List<Player>> teams;
			if (config.getBoolean("debugmode")) {
				teams = new ArrayList<List<Player>>();
				List<Player> p1 = new ArrayList<Player>();
				List<Player> p2 = new ArrayList<Player>();
				p1.add(players.get(0));
				p2.add(players.get(1));
				teams.add(p1);
				teams.add(p2);
			} else {
				List<Player> list=new ArrayList<Player>();
				list.addAll(players);
				int size=list.size();
				int sublistSize=size/4;
				teams=new ArrayList<List<Player>>();
				int lIndx=0;
				int rIndx=0;
				rIndx=sublistSize;
				for(int i=0;i<4;i++){
				  List<Player> subList=list.subList(lIndx,rIndx);
				  lIndx=rIndx;
				  if(i==2){
				    rIndx=size;
				  }else{
				    rIndx+=sublistSize;
				  }
				  teams.add(subList);
				}
			}
			System.out.println(teams);
			CyberhubBeaconwars.blueplayers.addAll(teams.get(0));
			for (Player p : CyberhubBeaconwars.blueplayers) {
				p.teleport((Location) CyberhubBeaconwars.blueteam.get("base"));
			}
			CyberhubBeaconwars.redplayers.addAll(teams.get(1));
			for (Player p : CyberhubBeaconwars.redplayers) {
				p.teleport((Location) CyberhubBeaconwars.redteam.get("base"));
			}
			if (!config.getBoolean("debugmode")) {
				CyberhubBeaconwars.greenplayers.addAll(teams.get(2));
				for (Player p : CyberhubBeaconwars.greenplayers) {
					p.teleport((Location) CyberhubBeaconwars.greenteam.get("base"));
				}
				CyberhubBeaconwars.yellowplayers.addAll(teams.get(3));
				for (Player p : CyberhubBeaconwars.yellowplayers) {
					p.teleport((Location) CyberhubBeaconwars.yellowteam.get("base"));
				}
			}
			CyberhubBeaconwars.players = players;
			CyberhubBeaconwars.deadteams = 4-teams.size();
			for (Player p : CyberhubBeaconwars.players) {
				p.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
				p.setHealth(20.0);
				p.setFoodLevel(20);
			}
			new BukkitRunnable() {

				@Override
				public void run() {
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
					if (CyberhubBeaconwars.deadteams == 3) {
						List<List<Player>> teams = new ArrayList<List<Player>>();
						teams.add(CyberhubBeaconwars.blueplayers);
						teams.add(CyberhubBeaconwars.redplayers);
						teams.add(CyberhubBeaconwars.greenplayers);
						teams.add(CyberhubBeaconwars.yellowplayers);
						for (int i=0; i<4; i++) {
							if (teams.get(i).size() != 0) {
								if (i==0) {
									Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Blue team won the game!");
								} else if (i==1) {
									Bukkit.broadcastMessage(ChatColor.RED + "Red team won the game!");
								} else if (i==2) {
									Bukkit.broadcastMessage(ChatColor.GREEN + "Green team won the game!");
								} else if (i==3) {
									Bukkit.broadcastMessage(ChatColor.YELLOW + "Yellow team won the game!");
								}
								CyberhubBeaconwars.enabled = false;
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
								for (int x=x1; x<x2+1; x++) {
									for (int y=y1; y<y2+1; y++) {
										for (int z=z1; z<z2+1; z++) {
											if (!CyberhubBeaconwars.blocks.contains(new Location(Bukkit.getWorld(config.getString("map")), x, y, z).getBlock())) {
												new Location(Bukkit.getWorld(config.getString("map")), x, y, z).getBlock().setType(Material.AIR);
											}
										}
									}
								}
								((Location) CyberhubBeaconwars.blueteam.get("beacon")).getBlock().setType(Material.AIR);
								((Location) CyberhubBeaconwars.redteam.get("beacon")).getBlock().setType(Material.AIR);
								((Location) CyberhubBeaconwars.greenteam.get("beacon")).getBlock().setType(Material.AIR);
								((Location) CyberhubBeaconwars.yellowteam.get("beacon")).getBlock().setType(Material.AIR);
								for (Player player : CyberhubBeaconwars.players) {
									player.getInventory().clear();
									player.performCommand("spawn");
								}
								this.cancel();
							}
						}
					}
				}
				
			}.runTaskTimer(beaconwars.plugin, 1, 1);
		}
	}
}
