package net.Cyberhub.tkdkid1000.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

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
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInHand().getType() == Material.FIREBALL) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Fireball fireball = player.launchProjectile(Fireball.class);
				fireball.setIsIncendiary(false);
				if (player.getInventory().getItemInHand().getAmount() == 1) {
					player.getInventory().setItemInHand(new ItemStack(Material.AIR));
				} else {
					event.getPlayer().getInventory().getItemInHand().setAmount(event.getPlayer().getInventory().getItemInHand().getAmount()-1);
				}
			}
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.BEACON) {
				event.getClickedBlock().setType(Material.AIR);
				for (int x=0; x<8; x++) {
					HashMap team = CyberhubBeaconwars.teamlist.get(x);
					List<UUID> players = CyberhubBeaconwars.playerlist.get(x);
					List<String> colors = CyberhubBeaconwars.colors;
					if (event.getClickedBlock().equals(((Location) team.get("beacon")).getBlock())) {
						if (players.contains(event.getPlayer().getUniqueId())) {
							event.setCancelled(true);
							event.getClickedBlock().setType(Material.BEACON);
							event.getPlayer().sendMessage(ChatColor.RED + "You can't shatter your own beacon!");
						} else {
							for (UUID uuid : CyberhubBeaconwars.players) {
								Player p = Bukkit.getPlayer(uuid);
								p.sendMessage(ChatColor.RED + player.getName() + " shattered " + colors.get(x) + "'s beacon!");
								team.replace("beaconalive", false);
								CyberhubBeaconwars.teamlist.set(x, team);
								for (UUID teamp : players) {
									Bukkit.getPlayer(teamp).sendTitle(ChatColor.RED + "Beacon Shatted!", ChatColor.GRAY + "You will no longer respawn.");
								}
							}
						}
					}
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
		if (event.getBlock().getType() == Material.TNT) {
			event.getBlock().setType(Material.AIR);
			Entity ent = event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);
			TNTPrimed tnt = (TNTPrimed) ent;
			tnt.setFuseTicks(40);
			tnt.setIsIncendiary(false);
		}
	}
	
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
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if (!CyberhubBeaconwars.enabled) return;
		if (player.hasPermission("cyberhubbeaconwars.override")) return;
		if (!player.getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		beaconwars.playerdata.getConfig().set("playerdata."+player.getUniqueId().toString()+".deaths", beaconwars.playerdata.getConfig().getInt("playerdata."+player.getUniqueId().toString()+".deaths")+1);
		event.setKeepInventory(true);
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[] {
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR)
		});
		if (player.getKiller() != null) {
			for (UUID uuid : CyberhubBeaconwars.players) {
				Player p = Bukkit.getPlayer(uuid);
				p.sendMessage(ChatColor.GRAY + event.getDeathMessage().replace(player.getName(), ChatColor.RED + player.getName() + ChatColor.GRAY).replace(player.getKiller().getName(), ChatColor.RED + player.getKiller().getName() + ChatColor.GRAY));
			}
		} else {
			for (UUID uuid : CyberhubBeaconwars.players) {
				Player p = Bukkit.getPlayer(uuid);
				p.sendMessage(ChatColor.GRAY + event.getDeathMessage().replace(player.getName(), ChatColor.RED + player.getName() + ChatColor.GRAY));
			}
		}
		event.setDeathMessage("");
		player.spigot().respawn();
		if (player.getKiller() != null) {
			try {
				Economy.add(player.getKiller().getName(), 5);
				player.getKiller().sendMessage(ChatColor.GOLD + "5 gold, Kill.");
				beaconwars.playerdata.getConfig().set("playerdata."+player.getKiller().getUniqueId().toString()+".kills", beaconwars.playerdata.getConfig().getInt("playerdata."+player.getKiller().getUniqueId().toString()+".kills")+1);
				beaconwars.playerdata.save();
			} catch (NoLoanPermittedException | UserDoesNotExistException e) {
				player.getKiller().sendMessage(ChatColor.RED + "Failed to give you your gold. Please contact a server admin.");
				e.printStackTrace();
			}
		}
		for (int x=0; x<8; x++) {
			HashMap team = CyberhubBeaconwars.teamlist.get(x);
			List<List<UUID>> players = CyberhubBeaconwars.playerlist;
			if (players.get(x).contains(player.getUniqueId())) {
				player.teleport((Location) team.get("base"));
				if ((boolean) team.get("beaconalive")) {
					player.setGameMode(GameMode.SPECTATOR);
					player.sendMessage(ChatColor.RED + "You died! You will respawn in " + config.getInt("respawntime") + " seconds!");
					new BukkitRunnable() {

						@Override
						public void run() {
							player.setGameMode(GameMode.SURVIVAL);
							player.sendMessage(ChatColor.DARK_BLUE + "You have respawned.");
							player.teleport((Location) team.get("base"));
						}
						
					}.runTaskLater(beaconwars, config.getInt("respawntime")*20);
				} else {
					Functions.elimPlayer(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!CyberhubBeaconwars.enabled) return;
		if (player.hasPermission("cyberhubbeaconwars.override")) return;
		if (!CyberhubBeaconwars.players.contains(player.getUniqueId())) return;
		player.getInventory().clear();
		player.setHealth(20.0);
		player.setFoodLevel(20);
		if (CyberhubBeaconwars.players.contains(player.getUniqueId())) {
			CyberhubBeaconwars.players.remove(player.getUniqueId());
		}
		for (UUID uuid : CyberhubBeaconwars.players) {
			Player p = Bukkit.getPlayer(uuid);
			p.sendMessage(ChatColor.RED + player.getName() + " quit the game.");
		}
		Functions.elimPlayer(player);
		player.performCommand("spawn");
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!CyberhubBeaconwars.enabled) return;
		if (event.getPlayer().hasPermission("cyberhubbeaconwars.override")) return;
		if (!CyberhubBeaconwars.players.contains(event.getPlayer().getUniqueId())) return;
		if (!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		if (event.getPlayer().getLocation().getY() < 0) {
			if (event.getPlayer().getHealth() != 0) {
				event.getPlayer().setHealth(0);
			}
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (!CyberhubBeaconwars.enabled) return;
		if (player.hasPermission("cyberhubbeaconwars.override")) return;
		if (!CyberhubBeaconwars.players.contains(player.getUniqueId())) return;
		if (player.getWorld().getName().equalsIgnoreCase(config.getString("spawnworld"))) {
			player.getInventory().clear();
			player.setHealth(20.0);
			player.setFoodLevel(20);
			if (CyberhubBeaconwars.players.contains(player.getUniqueId())) {
				CyberhubBeaconwars.players.remove(player.getUniqueId());
			}
			for (UUID uuid : CyberhubBeaconwars.players) {
				Player p = Bukkit.getPlayer(uuid);
				p.sendMessage(ChatColor.RED + player.getName() + " quit the game.");
			}
			Functions.elimPlayer(player);
			player.performCommand("spawn");
		}
	}
}
