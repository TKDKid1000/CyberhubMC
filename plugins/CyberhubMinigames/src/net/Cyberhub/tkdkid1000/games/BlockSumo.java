package net.Cyberhub.tkdkid1000.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.Cyberhub.tkdkid1000.CyberhubMinigames;
import net.Cyberhub.tkdkid1000.scripts.Locstring;
import net.Cyberhub.tkdkid1000.utils.BoundingBox;
import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.Cyberhub.tkdkid1000.utils.PotionBuilder;
import net.md_5.bungee.api.ChatColor;

public class BlockSumo implements Listener, CommandExecutor {

	private static HashMap<Player, Integer> lives = new HashMap<Player, Integer>();
	private static List<Player> players = new ArrayList<Player>();
	private static List<Block> blocks = new ArrayList<Block>();
	private static boolean enabled;
	private static BoundingBox box;
	private FileConfiguration config;
	private CyberhubMinigames minigames;
	private static int timer;

	public BlockSumo(CyberhubMinigames minigames, FileConfiguration config) {
		this.minigames = minigames;
		this.config = config;
	}
	
	public void setup() {
		minigames.getServer().getPluginManager().registerEvents(this, minigames);
		minigames.getCommand("blocksumo").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Please specify a sub command!");
			} else {
				if (args[0].equalsIgnoreCase("setworld")) {
					player.sendMessage(ChatColor.GREEN + "Set the world to " + player.getWorld().getName());
					config.set("blocksumo.world", player.getWorld().getName());
					minigames.saveConfig();
				} else if (args[0].equalsIgnoreCase("setvoid")) {
					if (args.length == 1) {
						player.sendMessage(ChatColor.RED + "Specify a y level!");
					} else {
						player.sendMessage(ChatColor.GREEN + "Set the y to " + args[1]);
						config.set("blocksumo.void", Integer.parseInt(args[1]));
						minigames.saveConfig();
					}
				} else if (args[0].equalsIgnoreCase("setpos1")) {
					config.set("blocksumo.boundingbox.x1", player.getLocation().getBlockX());
					config.set("blocksumo.boundingbox.y1", player.getLocation().getBlockY());
					config.set("blocksumo.boundingbox.z1", player.getLocation().getBlockZ());
					minigames.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set the bounding box 1 to your location.");
				} else if (args[0].equalsIgnoreCase("setpos2")) {
					config.set("blocksumo.boundingbox.x2", player.getLocation().getBlockX());
					config.set("blocksumo.boundingbox.y2", player.getLocation().getBlockY());
					config.set("blocksumo.boundingbox.z2", player.getLocation().getBlockZ());
					minigames.saveConfig();
					player.sendMessage(ChatColor.GREEN + "Set the bounding box 2 to your location.");
				} else if (args[0].equalsIgnoreCase("setlives")) {
					if (args.length == 1) {
						player.sendMessage(ChatColor.RED + "Specify a lives count!");
					} else {
						player.sendMessage(ChatColor.GREEN + "Set the lives count to " + args[1]);
						config.set("blocksumo.lives", Integer.parseInt(args[1]));
						minigames.saveConfig();
					}
				} else if (args[0].equalsIgnoreCase("setcenter")) {
					player.sendMessage(ChatColor.GREEN + "Set the center to your location.");
					config.set("blocksumo.center", player.getLocation().getBlockX() + ","
							+ player.getLocation().getBlockY() + ","
							+ player.getLocation().getBlockZ() + ",");
					minigames.saveConfig();
				} else if (args[0].equalsIgnoreCase("setspawn")) {
					String locstring = player.getLocation().getBlockX()+","+
							player.getLocation().getBlockY()+","+
							player.getLocation().getBlockZ()+",";
					List<String> spawns = config.getStringList("blocksumo.spawns");
					if (spawns.contains(locstring)) {
						spawns.remove(locstring);
						config.set("blocksumo.spawns", spawns);
						minigames.saveConfig();
						player.sendMessage(ChatColor.GREEN + "Removed the currently set spawnpoint");
					} else {
						spawns.add(locstring);
						config.set("blocksumo.spawns", spawns);
						minigames.saveConfig();
						player.sendMessage(ChatColor.GREEN + "Set a spawnpoint at your current location");
					}
				}
			}
		}
		return true;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!enabled) return;
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(config.getString("blocksumo.world"))) {
			if (event.getPlayer().getLocation().getY() < config.getInt("blocksumo.void")) {
				Player player = event.getPlayer();
				randomSpawn(player);
				if (player.getWorld().getName().equalsIgnoreCase(config.getString("blocksumo.world"))) {
					World world = Bukkit.getWorld(config.getString("blocksumo.world"));
					player.spigot().respawn();
					if (players.contains(player) && lives.containsKey(player)) {
						if (lives.get(player) >= 1) {
							player.sendMessage(ChatColor.RED + "You died! You have " + lives.get(player) + " lives left.");
							for (Player p : world.getPlayers()) {
								p.sendMessage(ChatColor.RED + player.getName() + " died! The have " + lives.get(player) + " lives left.");
							}
							lives.replace(player, lives.get(player)-1);
						} else {
							player.sendMessage(ChatColor.RED + "You have been eliminated!");
							for (Player p : world.getPlayers()) {
								p.sendMessage(ChatColor.RED + player.getName() + " has been eliminated.");
							}
							player.setGameMode(GameMode.SPECTATOR);
							lives.remove(player);
							players.remove(player);
						}
					} else {
						player.performCommand("spawn");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (!enabled) return;
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.getWorld().getName().equalsIgnoreCase(config.getString("blocksumo.world"))) {
				World world = Bukkit.getWorld(config.getString("blocksumo.world"));
				player.spigot().respawn();
				if (players.contains(player) && lives.containsKey(player)) {
					if (lives.get(player) >= 1) {
						randomSpawn(player);
						player.sendMessage(ChatColor.RED + "You died! You have " + lives.get(player) + " lives left.");
						for (Player p : world.getPlayers()) {
							p.sendMessage(ChatColor.RED + player.getName() + " died! The have " + lives.get(player) + " lives left.");
						}
						lives.replace(player, lives.get(player)-1);
					} else {
						player.sendMessage(ChatColor.RED + "You have been eliminated!");
						for (Player p : world.getPlayers()) {
							p.sendMessage(ChatColor.RED + player.getName() + " has been eliminated.");
						}
						player.setGameMode(GameMode.SPECTATOR);
						lives.remove(player);
						players.remove(player);
					}
				} else {
					player.performCommand("spawn");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!enabled) return;
		if (!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		if (!new BoundingBox(config.getInt("blocksumo.boundingbox.x1"), 
				config.getInt("blocksumo.boundingbox.y1"), 
				config.getInt("blocksumo.boundingbox.z1"), 
				config.getInt("blocksumo.boundingbox.x2"), 
				config.getInt("blocksumo.boundingbox.y2"), 
				config.getInt("blocksumo.boundingbox.z2")).contains(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You can't place blocks here!");
		}
		if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOOL) {
			if (event.getPlayer().getInventory().getItemInMainHand().getAmount() != 64) {
				event.getPlayer().getInventory().getItemInMainHand().setAmount(64);
			}
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
		if (!enabled) return;
		if (!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("map")));
		if (blocks.contains(event.getBlock())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You can't break blocks here!");
		}
	}
	
	public void start() {
		World world = Bukkit.getWorld(config.getString("blocksumo.world"));
		blocks.clear();
		players.clear();
		lives.clear();
		List<String> locstrings = config.getStringList("blocksumo.spawns");
		List<Location> spawns = new ArrayList<Location>();
		for (String locstring : locstrings) {
			spawns.add(Locstring.getLocationInt(locstring, Bukkit.getWorld(config.getString("blocksumo.world"))));
		}
		for (Location spawn : spawns) {
			spawn.subtract(0, 1, 0).getBlock().setType(Material.AIR);
		}
		box = new BoundingBox(config.getInt("blocksumo.boundingbox.x1"), 
				config.getInt("blocksumo.boundingbox.y1"), 
				config.getInt("blocksumo.boundingbox.z1"), 
				config.getInt("blocksumo.boundingbox.x2"), 
				config.getInt("blocksumo.boundingbox.y2"), 
				config.getInt("blocksumo.boundingbox.z2"));
		int x1 = (int) box.getMinX();
		int y1 = (int) box.getMinY();
		int z1 = (int) box.getMinZ();
		int x2 = (int) box.getMaxX();
		int y2 = (int) box.getMaxY();
		int z2 = (int) box.getMaxZ();
		for (int x=x1; x<x2+1; x++) {
			for (int y=y1; y<y2+1; y++) {
				for (int z=z1; z<z2+1; z++) {
					if (!(new Location(world, x, y, z).getBlock().getType() == Material.AIR)) {
						blocks.add(new Location(world, x, y, z).getBlock());
					}
				}
			}
		}
		for (Player player : world.getPlayers()) {
			player.getInventory().clear();
			player.getInventory().addItem(new ItemStack(Material.WOOL, 64));
			player.getInventory().addItem(new ItemBuilder(Material.SHEARS, 1)
					.addEnchant(Enchantment.DIG_SPEED, 2)
					.setUnbreakable(true)
					.build());
			players.add(player);
			lives.put(player, 5);
			randomSpawn(player);
		}
		timer=0;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Player p : players) {
					p.setHealth(20);
					p.setFoodLevel(20);
				}
				timer++;
				if (timer == 20*20) {
					for (Player p : players) {
						p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 4));
						p.sendMessage(ChatColor.GREEN + "All players received 4 snowballs.");
					}
				}
				if (timer == 40*20) {
					for (Player p : players) {
						p.getInventory().addItem(new ItemStack(Material.FIREBALL, 1));
						p.sendMessage(ChatColor.GREEN + "All players received a fireball.");
					}
				}
				if (timer == 60*20) {
					for (Player p : players) {
						p.getInventory().addItem(new ItemBuilder(Material.WOOD_SWORD, 1)
								.addEnchant(Enchantment.KNOCKBACK, 1)
								.setDurability((short)59)
								.build());
						p.sendMessage(ChatColor.GREEN + "All players received knockback sword.");
					}
				}
				if (timer == 80*20) {
					for (Player p : players) {
						p.getInventory().addItem(new ItemStack(Material.TNT, 1));
						p.sendMessage(ChatColor.GREEN + "All players received a tnt.");
					}
				}
				if (timer == 100*20) {
					for (Player p : players) {
						p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "An op item is spawning at mid in 5 seconds!");
					}
				}
				if (timer == 105*20) {
					Location loc = Locstring.getLocationInt(config.getString("blocksumo.center"), world).add(0, 1, 0);
					int itemno = new Random().nextInt(4);
					if (itemno == 0) {
						world.dropItem(loc, new ItemBuilder(Material.DIAMOND_SWORD, 1)
								.addEnchant(Enchantment.KNOCKBACK, 10)
								.setDurability((short)1561)
								.setName(ChatColor.RED + "Rocket Sword")
								.build());
						for (Player p : players) {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Rocket Sword spawned!");
						}
					} else if (itemno == 1) {
						world.dropItem(loc, new PotionBuilder(1)
								.addEffect(PotionEffectType.JUMP, 30*20, 5, true, false)
								.build());
						for (Player p : players) {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Jump Potion spawned!");
						}
					} else if (itemno == 2) {
						world.dropItem(loc, new PotionBuilder(1)
								.addEffect(PotionEffectType.SPEED, 30*20, 5, true, false)
								.build());
						for (Player p : players) {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Speed Potion spawned!");
						}
					} else if (itemno == 3) {
						world.dropItem(loc, new ItemBuilder(Material.EYE_OF_ENDER, 1)
								.setName(ChatColor.RED + "Extra Life")
								.build());
						for (Player p : players) {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Extra Life spawned!");
						}
					}
					timer=0;
				}
				
				if (players.size() == 1) {
					Bukkit.broadcastMessage(ChatColor.GREEN + players.get(0).getName() + " won block sumo!");
					enabled = false;
					box = new BoundingBox(config.getInt("blocksumo.boundingbox.x1"), 
							config.getInt("blocksumo.boundingbox.y1"), 
							config.getInt("blocksumo.boundingbox.z1"), 
							config.getInt("blocksumo.boundingbox.x2"), 
							config.getInt("blocksumo.boundingbox.y2"), 
							config.getInt("blocksumo.boundingbox.z2"));
					int x1 = (int) box.getMinX();
					int y1 = (int) box.getMinY();
					int z1 = (int) box.getMinZ();
					int x2 = (int) box.getMaxX();
					int y2 = (int) box.getMaxY();
					int z2 = (int) box.getMaxZ();
					for (int x=x1; x<x2+1; x++) {
						for (int y=y1; y<y2+1; y++) {
							for (int z=z1; z<z2+1; z++) {
								if (!blocks.contains(new Location(world, x, y, z).getBlock())) {
									new Location(world, x, y, z).getBlock().setType(Material.AIR);
								}
							}
						}
					}
					for (Player p : world.getPlayers()) {
						p.performCommand("spawn");
					}
					this.cancel();
				}
			}
			
		}.runTaskTimer(minigames, 1, 1);
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (enabled) return;
		if (player.getWorld().getName().equalsIgnoreCase(config.getString("blocksumo.world"))) {
			if (player.getWorld().getPlayers().size() >= config.getInt("blocksumo.minplayers")) {
				for (Player p : event.getPlayer().getWorld().getPlayers()) {
					p.sendMessage(ChatColor.GOLD + "Game starts in 10 seconds.");
				}
				int[] loop = {5,4,3,2,1};
				for (int x : loop) {
					new BukkitRunnable() {

						@Override
						public void run() {
							for (Player p : event.getPlayer().getWorld().getPlayers()) {
								p.sendMessage(ChatColor.GOLD + "Game starts in " + (6-x) + " seconds.");
							}
						}
						
					}.runTaskLater(minigames, x*20);
				}
				new BukkitRunnable() {

					@Override
					public void run() {
						for (Player p : event.getPlayer().getWorld().getPlayers()) {
							p.sendMessage(ChatColor.GOLD + "Game started!");
						}
						start();
						enabled = true;
					}
					
				}.runTaskLater(minigames, 5*20);
			}
		}
	}
	
	public void randomSpawn(Player player) {
		List<String> locstrings = config.getStringList("blocksumo.spawns");
		List<Location> spawns = new ArrayList<Location>();
		for (String locstring : locstrings) {
			spawns.add(Locstring.getLocationInt(locstring, Bukkit.getWorld(config.getString("blocksumo.world"))));
		}
		Location spawn = spawns.get(new Random().nextInt(spawns.size()));
		spawn.clone().subtract(0, 1, 0).getBlock().setType(Material.WOOL);
		player.teleport(spawn.add(0, 1, 0));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!enabled) return;
		if (!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("blocksumo.world")));
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().getType() == Material.FIREBALL) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Fireball fireball = player.launchProjectile(Fireball.class);
				fireball.setIsIncendiary(false);
				if (player.getInventory().getItemInMainHand().getAmount() == 1) {
					player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
				} else {
					event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
				}
			}
		} else if (player.getInventory().getItemInMainHand().getType() == Material.EYE_OF_ENDER) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				lives.replace(player, lives.get(player)+1);
				player.sendMessage(ChatColor.GREEN + "+1 lives!");
			}
		}
	}
	
	@EventHandler
	public void onTnt(EntityExplodeEvent event) {
		if (!enabled) return;
		if (!event.getLocation().getWorld().getName().equalsIgnoreCase(config.getString("blocksumo.world")));
		if (!new BoundingBox(config.getInt("blocksumo.boundingbox.x1"), 
				config.getInt("blocksumo.boundingbox.y1"), 
				config.getInt("blocksumo.boundingbox.z1"), 
				config.getInt("blocksumo.boundingbox.x2"), 
				config.getInt("blocksumo.boundingbox.y2"), 
				config.getInt("blocksumo.boundingbox.z2")).contains(event.getLocation().getX(), event.getLocation().getY(), event.getLocation().getZ())) return;
		if (event.getEntity() instanceof TNTPrimed || event.getEntity() instanceof Fireball) {
			List<Block> tntblocks = new ArrayList<Block>();
			tntblocks.addAll(event.blockList());
			for (Block block : tntblocks) {
				if (blocks.contains(block)) {
					event.blockList().remove(block);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (players.contains(player)) {
				event.setDamage(0);
			}
		}
	}
}
