package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;
import net.Cyberhub.tkdkid1000.utils.BoundingBox;
import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {

	private CyberhubBeaconwars beaconwars;
	private FileConfiguration config;
	
	public Events(CyberhubBeaconwars beaconwars) {
		this.beaconwars = beaconwars;
		this.config = beaconwars.config;
	}
	
	public void register() {
		System.out.println("registered!");
		beaconwars.getServer().getPluginManager().registerEvents(this, beaconwars);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInHand().getType() == Material.FIREBALL) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				player.launchProjectile(Fireball.class);
				if (player.getInventory().getItemInHand().getAmount() == 1) {
					player.getInventory().setItemInHand(new ItemStack(Material.AIR));
				} else {
					event.getPlayer().getInventory().getItemInHand().setAmount(event.getPlayer().getInventory().getItemInHand().getAmount()-1);
				}
			}
		}
	}
	
	@EventHandler
	public void onTnt(EntityExplodeEvent event) {
		if (!CyberhubBeaconwars.enabled) return;
		if (!event.getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		if (!new BoundingBox(config.getInt("boundingbox.x1"), 
				config.getInt("boundingbox.y1"), 
				config.getInt("boundingbox.z1"), 
				config.getInt("boundingbox.x2"), 
				config.getInt("boundingbox.y2"), 
				config.getInt("boundingbox.z2")).contains(event.getLocation().getX(), event.getLocation().getY(), event.getLocation().getZ())) return;
		if (event.getEntity() instanceof TNTPrimed || event.getEntity() instanceof Fireball) {
			List<Block> tntblocks = new ArrayList<Block>();
			tntblocks.addAll(event.blockList());
			System.out.println(tntblocks);
			for (Block block : tntblocks) {
				if (CyberhubBeaconwars.blocks.contains(block)) {
					event.blockList().remove(block);
				} else if (block.getType() == Material.BEACON) {
					event.blockList().remove(block);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!CyberhubBeaconwars.enabled) return;
		if (event.getPlayer().hasPermission("cyberhubbeaconwars.override")) return;
		if (!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		if (!new BoundingBox(config.getInt("boundingbox.x1"), 
				config.getInt("boundingbox.y1"), 
				config.getInt("boundingbox.z1"), 
				config.getInt("boundingbox.x2"), 
				config.getInt("boundingbox.y2"), 
				config.getInt("boundingbox.z2")).contains(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You can't place blocks here!");
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!CyberhubBeaconwars.enabled) return;
		if (event.getPlayer().hasPermission("cyberhubbeaconwars.override")) return;
		if (!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		if (CyberhubBeaconwars.blocks.contains(event.getBlock())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You can't break blocks here!");
		}
		if (config.getBoolean("debugmode")) {
			event.getPlayer().sendMessage(event.getBlock().getLocation()+"");
		}
		if (event.getBlock().equals(((Location) CyberhubBeaconwars.blueteam.get("beacon")).getBlock())) {
			if (!(boolean)CyberhubBeaconwars.blueteam.get("beaconalive")) return;
			if (CyberhubBeaconwars.blueplayers.contains(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't shatter your own beacon!");
				event.setCancelled(true);
				return;
			}
			event.getBlock().setType(Material.AIR);
			if (CyberhubBeaconwars.blocks.contains(event.getBlock())) {
				CyberhubBeaconwars.blocks.remove(event.getBlock());
			}
			CyberhubBeaconwars.blueteam.replace("beaconalive", false);
			for (Player p : CyberhubBeaconwars.players) {
				p.sendMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + " shattered blue teams beacon!");
			}
			for (Player p : CyberhubBeaconwars.blueplayers) {
				p.sendTitle(ChatColor.DARK_RED + "You beacon has been shattered!", ChatColor.DARK_RED + "You can no longer respawn!");
			}
		}
		if (event.getBlock().equals(((Location) CyberhubBeaconwars.redteam.get("beacon")).getBlock())) {
			if (!(boolean)CyberhubBeaconwars.redteam.get("beaconalive")) return;
			if (CyberhubBeaconwars.redplayers.contains(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't shatter your own beacon!");
				event.setCancelled(true);
				return;
			}
			event.getBlock().setType(Material.AIR);
			if (CyberhubBeaconwars.blocks.contains(event.getBlock())) {
				CyberhubBeaconwars.blocks.remove(event.getBlock());
			}
			CyberhubBeaconwars.redteam.replace("beaconalive", false);
			for (Player p : CyberhubBeaconwars.players) {
				p.sendMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + " shattered red teams beacon!");
			}
			for (Player p : CyberhubBeaconwars.redplayers) {
				p.sendTitle(ChatColor.DARK_RED + "You beacon has been shattered!", ChatColor.DARK_RED + "You can no longer respawn!");
			}
		}
		if (event.getBlock().equals(((Location) CyberhubBeaconwars.greenteam.get("beacon")).getBlock())) {
			if (!(boolean)CyberhubBeaconwars.greenteam.get("beaconalive")) return;
			if (CyberhubBeaconwars.greenplayers.contains(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't shatter your own beacon!");
				event.setCancelled(true);
				return;
			}
			event.getBlock().setType(Material.AIR);
			if (CyberhubBeaconwars.blocks.contains(event.getBlock())) {
				CyberhubBeaconwars.blocks.remove(event.getBlock());
			}
			CyberhubBeaconwars.greenteam.replace("beaconalive", false);
			for (Player p : CyberhubBeaconwars.players) {
				p.sendMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + " shattered green teams beacon!");
			}
			for (Player p : CyberhubBeaconwars.greenplayers) {
				p.sendTitle(ChatColor.DARK_RED + "You beacon has been shattered!", ChatColor.DARK_RED + "You can no longer respawn!");
			}
		}
		if (event.getBlock().equals(((Location) CyberhubBeaconwars.yellowteam.get("beacon")).getBlock())) {
			if (!(boolean)CyberhubBeaconwars.yellowteam.get("beaconalive")) return;
			if (CyberhubBeaconwars.yellowplayers.contains(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't shatter your own beacon!");
				event.setCancelled(true);
				return;
			}
			event.getBlock().setType(Material.AIR);
			if (CyberhubBeaconwars.blocks.contains(event.getBlock())) {
				CyberhubBeaconwars.blocks.remove(event.getBlock());
			}
			CyberhubBeaconwars.yellowteam.replace("beaconalive", false);
			for (Player p : CyberhubBeaconwars.players) {
				p.sendMessage(ChatColor.GRAY + event.getPlayer().getDisplayName() + " shattered yellow teams beacon!");
			}
			for (Player p : CyberhubBeaconwars.yellowplayers) {
				p.sendTitle(ChatColor.DARK_RED + "You beacon has been shattered!", ChatColor.DARK_RED + "You can no longer respawn!");
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if (!CyberhubBeaconwars.enabled) return;
		if (player.hasPermission("cyberhubbeaconwars.override")) return;
		if (!player.getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		event.setKeepInventory(true);
		player.spigot().respawn();
		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
		for (Player p : CyberhubBeaconwars.players) {
			p.sendMessage(ChatColor.DARK_BLUE + event.getDeathMessage());
		}
		event.setDeathMessage("");
		//blue
		if (Functions.getTeam(player).equals("b")) {
			if ((boolean) CyberhubBeaconwars.blueteam.get("beaconalive")) {
				BoundingBox bb = new BoundingBox(config.getInt("boundingbox.x1"), 
						config.getInt("boundingbox.y1"), 
						config.getInt("boundingbox.z1"), 
						config.getInt("boundingbox.x2"), 
						config.getInt("boundingbox.y2"), 
						config.getInt("boundingbox.z2"));
				double x = bb.getCenterX();
				double y = bb.getCenterY();
				double z = bb.getCenterZ();
				player.teleport(new Location(Bukkit.getWorld(config.getString("map")), x, y, z));
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage(ChatColor.RED + "You died! You will respawn in " + config.getLong("respawntime")*20 + " seconds.");
				new BukkitRunnable() {

					@Override
					public void run() {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.GREEN + "You have respawned.");
						player.teleport((Location) CyberhubBeaconwars.blueteam.get("base"));
					}
					
				}.runTaskLater(beaconwars.plugin, config.getLong("respawntime")*20);
			} else {
				player.sendMessage(ChatColor.RED + "You have been eliminated from the game!");
				player.getInventory().clear();
				CyberhubBeaconwars.blueplayers.remove(player);
				CyberhubBeaconwars.players.remove(player);
				player.performCommand("spawn");
				if (CyberhubBeaconwars.blueplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Blue team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
		} 
		//red
		else if (Functions.getTeam(player).equals("r")) {
			if ((boolean) CyberhubBeaconwars.redteam.get("beaconalive")) {
				BoundingBox bb = new BoundingBox(config.getInt("boundingbox.x1"), 
						config.getInt("boundingbox.y1"), 
						config.getInt("boundingbox.z1"), 
						config.getInt("boundingbox.x2"), 
						config.getInt("boundingbox.y2"), 
						config.getInt("boundingbox.z2"));
				double x = bb.getCenterX();
				double y = bb.getCenterY();
				double z = bb.getCenterZ();
				player.teleport(new Location(Bukkit.getWorld(config.getString("map")), x, y, z));
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage(ChatColor.RED + "You died! You will respawn in " + config.getLong("respawntime")*20 + " seconds.");
				new BukkitRunnable() {

					@Override
					public void run() {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.GREEN + "You have respawned.");
						player.teleport((Location) CyberhubBeaconwars.redteam.get("base"));
					}
					
				}.runTaskLater(beaconwars.plugin, config.getLong("respawntime")*20);
			} else {
				player.sendMessage(ChatColor.RED + "You have been eliminated from the game!");
				player.getInventory().clear();
				CyberhubBeaconwars.redplayers.remove(player);
				CyberhubBeaconwars.players.remove(player);
				player.performCommand("spawn");
				if (CyberhubBeaconwars.redplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Red team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
		}
		//yellow
		else if (Functions.getTeam(player).equals("y")) {
			if ((boolean) CyberhubBeaconwars.yellowteam.get("beaconalive")) {
				BoundingBox bb = new BoundingBox(config.getInt("boundingbox.x1"), 
						config.getInt("boundingbox.y1"), 
						config.getInt("boundingbox.z1"), 
						config.getInt("boundingbox.x2"), 
						config.getInt("boundingbox.y2"), 
						config.getInt("boundingbox.z2"));
				double x = bb.getCenterX();
				double y = bb.getCenterY();
				double z = bb.getCenterZ();
				player.teleport(new Location(Bukkit.getWorld(config.getString("map")), x, y, z));
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage(ChatColor.RED + "You died! You will respawn in " + config.getLong("respawntime")*20 + " seconds.");
				new BukkitRunnable() {

					@Override
					public void run() {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.GREEN + "You have respawned.");
						player.teleport((Location) CyberhubBeaconwars.yellowteam.get("base"));
					}
					
				}.runTaskLater(beaconwars.plugin, config.getLong("respawntime")*20);
			} else {
				player.sendMessage(ChatColor.RED + "You have been eliminated from the game!");
				player.getInventory().clear();
				CyberhubBeaconwars.yellowplayers.remove(player);
				CyberhubBeaconwars.players.remove(player);
				player.performCommand("spawn");
				if (CyberhubBeaconwars.yellowplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Yellow team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
		}
		//green
		else if (Functions.getTeam(player).equals("g")) {
			if ((boolean) CyberhubBeaconwars.greenteam.get("beaconalive")) {
				BoundingBox bb = new BoundingBox(config.getInt("boundingbox.x1"), 
						config.getInt("boundingbox.y1"), 
						config.getInt("boundingbox.z1"), 
						config.getInt("boundingbox.x2"), 
						config.getInt("boundingbox.y2"), 
						config.getInt("boundingbox.z2"));
				double x = bb.getCenterX();
				double y = bb.getCenterY();
				double z = bb.getCenterZ();
				player.teleport(new Location(Bukkit.getWorld(config.getString("map")), x, y, z));
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage(ChatColor.RED + "You died! You will respawn in " + config.getLong("respawntime")*20 + " seconds.");
				new BukkitRunnable() {

					@Override
					public void run() {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.GREEN + "You have respawned.");
						player.teleport((Location) CyberhubBeaconwars.greenteam.get("base"));
					}
					
				}.runTaskLater(beaconwars.plugin, config.getLong("respawntime")*20);
			} else {
				player.sendMessage(ChatColor.RED + "You have been eliminated from the game!");
				player.getInventory().clear();
				CyberhubBeaconwars.greenplayers.remove(player);
				CyberhubBeaconwars.players.remove(player);
				player.performCommand("spawn");
				if (CyberhubBeaconwars.greenplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Green team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!CyberhubBeaconwars.enabled) return;
		if (player.hasPermission("cyberhubbeaconwars.override")) return;
		player.getInventory().clear();
		player.setHealth(20.0);
		player.setFoodLevel(20);
		if (CyberhubBeaconwars.players.contains(player)) {
			CyberhubBeaconwars.players.remove(player);
		}
		if (CyberhubBeaconwars.blueplayers.contains(player)) {
			CyberhubBeaconwars.blueplayers.remove(player);
			if (CyberhubBeaconwars.blueplayers.size() == 0) {
				for (Player p : CyberhubBeaconwars.players) {
					p.sendMessage(ChatColor.GRAY + "Blue team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
				}
				CyberhubBeaconwars.deadteams++;
			}
		}
		if (CyberhubBeaconwars.redplayers.contains(player)) {
			CyberhubBeaconwars.redplayers.remove(player);
			if (CyberhubBeaconwars.redplayers.size() == 0) {
				for (Player p : CyberhubBeaconwars.players) {
					p.sendMessage(ChatColor.GRAY + "Red team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
				}
				CyberhubBeaconwars.deadteams++;
			}
		}
		if (CyberhubBeaconwars.greenplayers.contains(player)) {
			CyberhubBeaconwars.greenplayers.remove(player);
			if (CyberhubBeaconwars.greenplayers.size() == 0) {
				for (Player p : CyberhubBeaconwars.players) {
					p.sendMessage(ChatColor.GRAY + "Green team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
				}
				CyberhubBeaconwars.deadteams++;
			}
		}
		if (CyberhubBeaconwars.yellowplayers.contains(player)) {
			CyberhubBeaconwars.yellowplayers.remove(player);
			if (CyberhubBeaconwars.yellowplayers.size() == 0) {
				for (Player p : CyberhubBeaconwars.players) {
					p.sendMessage(ChatColor.GRAY + "Yellow team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
				}
				CyberhubBeaconwars.deadteams++;
			}
		}
		for (Player p : CyberhubBeaconwars.players) {
			p.sendMessage(ChatColor.RED + p.getName() + " quit the game.");
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!CyberhubBeaconwars.enabled) return;
		if (player.hasPermission("cyberhubbeaconwars.override")) return;
		if (!player.getWorld().getName().equalsIgnoreCase(config.getString("map"))) {
			player.getInventory().clear();
			player.setHealth(20.0);
			player.setFoodLevel(20);
			if (CyberhubBeaconwars.players.contains(player)) {
				CyberhubBeaconwars.players.remove(player);
			}
			if (CyberhubBeaconwars.blueplayers.contains(player)) {
				CyberhubBeaconwars.blueplayers.remove(player);
				if (CyberhubBeaconwars.blueplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Blue team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
			if (CyberhubBeaconwars.redplayers.contains(player)) {
				CyberhubBeaconwars.redplayers.remove(player);
				if (CyberhubBeaconwars.redplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Red team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
			if (CyberhubBeaconwars.greenplayers.contains(player)) {
				CyberhubBeaconwars.greenplayers.remove(player);
				if (CyberhubBeaconwars.greenplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Green team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
			if (CyberhubBeaconwars.yellowplayers.contains(player)) {
				CyberhubBeaconwars.yellowplayers.remove(player);
				if (CyberhubBeaconwars.yellowplayers.size() == 0) {
					for (Player p : CyberhubBeaconwars.players) {
						p.sendMessage(ChatColor.GRAY + "Yellow team has been " + ChatColor.BOLD + ChatColor.DARK_RED + "Eliminated.");
					}
					CyberhubBeaconwars.deadteams++;
				}
			}
			for (Player p : CyberhubBeaconwars.players) {
				p.sendMessage(ChatColor.RED + p.getName() + " quit the game.");
			}
		}
	}
}
