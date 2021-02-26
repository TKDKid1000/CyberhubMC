package net.Cyberhub.tkdkid1000;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class GadgetGUI implements Listener, CommandExecutor {

	private List<Player> diamondtrailplayers = new ArrayList<Player>();
	private Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Gadgets");
	private CyberhubLobby lobby;
	private Map<String, Map<String, Boolean>> itemcooldown = new HashMap<String, Map<String, Boolean>>();
	
	public GadgetGUI(CyberhubLobby lobby) {
		this.lobby = lobby;
	}
	
	public void setup() {
		lobby.getCommand("gadget").setExecutor(this);
		lobby.getServer().getPluginManager().registerEvents(this, lobby);
	}
	
	public void createInv(Player player) {
		gui.clear();
		gui.setItem(9, new ItemBuilder(Material.BLAZE_ROD, 1)
				.setName(ChatColor.RED + "Firework Gun")
				.build());
		gui.setItem(11, new ItemBuilder(Material.FISHING_ROD, 1)
				.setName(ChatColor.RED + "Grappling Hook")
				.setUnbreakable(true)
				.build());
		gui.setItem(13, new ItemBuilder(Material.SADDLE, 1)
				.setName(ChatColor.RED + "Player Saddle")
				.build());
		gui.setItem(15, new ItemBuilder(Material.EMERALD, 1)
				.setName(ChatColor.RED + "Villager Launcher")
				.build());
		gui.setItem(17, new ItemBuilder(Material.DIAMOND_BLOCK, 1)
				.setName(ChatColor.RED + "Diamond Trail")
				.build());
		gui.setItem(45, new ItemBuilder(Material.SUGAR, 1)
				.setName(ChatColor.BLUE + "Trails")
				.build());
		gui.setItem(53, new ItemBuilder(Material.BARRIER, 1)
				.setName(ChatColor.DARK_RED + "Clear Gadgets")
				.build());
		gui.setItem(49, new ItemBuilder(Material.DIAMOND, 1)
				.setName(ChatColor.RED + "Gadget Shop")
				.build());
		for (int x=0; x<gui.getSize(); x++) {
			if (gui.getItem(x) == null) {
				gui.setItem(x, new ItemBuilder(Material.STAINED_GLASS_PANE, 1)
						.setName(" ")
						.build());
			}
		}
		player.openInventory(gui);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (!(event.getInventory().getName() == gui.getName())) return;
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() == null) return;
		ItemStack clicked = event.getCurrentItem();
		event.setCancelled(true);
		if (clicked.getType() == Material.GLASS) return;
		if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Grappling Hook")
				&& clicked.getType() == Material.FISHING_ROD) {
			if (player.hasPermission("cyberhublobby.gadget.fishingrod")) {
				player.getInventory().addItem(clicked);
			} else {
				player.sendMessage(ChatColor.RED + "You don't own that gadget!");
			}
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Firework Gun")
				&& clicked.getType() == Material.BLAZE_ROD) {
			if (player.hasPermission("cyberhublobby.gadget.fireworkgun")) {
				player.getInventory().addItem(clicked);
			} else {
				player.sendMessage(ChatColor.RED + "You don't own that gadget!");
			}
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Player Saddle")
				&& clicked.getType() == Material.SADDLE) {
			if (player.hasPermission("cyberhublobby.gadget.playersaddle")) {
				player.getInventory().addItem(clicked);
			} else {
				player.sendMessage(ChatColor.RED + "You don't own that gadget!");
			}
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Villager Launcher")
				&& clicked.getType() == Material.EMERALD) {
			if (player.hasPermission("cyberhublobby.gadget.villagerlauncher")) {
				player.getInventory().addItem(clicked);
			} else {
				player.sendMessage(ChatColor.RED + "That gadget requires VIP rank!");
			}
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Diamond Trail")
				&& clicked.getType() == Material.DIAMOND_BLOCK) {
			if (player.hasPermission("cyberhublobby.gadget.diamondtrail")) {
				player.getInventory().addItem(clicked);
			} else {
				player.sendMessage(ChatColor.RED + "That gadget requires MVP rank!");
			}
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Trails")
				&& clicked.getType() == Material.SUGAR) {
			player.performCommand("trails");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Gadget Shop")
				&& clicked.getType() == Material.DIAMOND) {
			player.performCommand("gadgetshop");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Clear Gadgets")
				&& clicked.getType() == Material.BARRIER) {
			player.getInventory().clear();
			player.sendMessage(ChatColor.GREEN + "Cleared your gadgets.");
			player.getInventory().setItem(4, new ItemBuilder(Material.COMPASS, 1)
					.setName(ChatColor.GREEN + "Server Switcher")
					.build());
			player.getInventory().setItem(2, new ItemBuilder(Material.REDSTONE, 1)
					.setName(ChatColor.GREEN + "Gadgets")
					.build());
			player.getInventory().setItem(6, new ItemBuilder(Material.DIAMOND, 1)
					.setName(ChatColor.GREEN + "Donate")
					.build());
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		itemcooldown.putIfAbsent(player.getUniqueId().toString(), new HashMap<String, Boolean>());
		if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
		if (item.getType() == Material.REDSTONE
				&& item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Gadgets")) {
			createInv(player);
		}
		if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Grappling Hook")
				&& item.getType() == Material.FISHING_ROD) {
			itemcooldown.get(player.getUniqueId().toString()).putIfAbsent(ChatColor.RED + "Grappling Hook", false);
			if (!itemcooldown.get(player.getUniqueId().toString()).get(ChatColor.RED + "Grappling Hook")) {
				player.setVelocity(player.getLocation().getDirection().normalize().multiply(3));
				itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Grappling Hook", true);
				new BukkitRunnable() {

					@Override
					public void run() {
						itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Grappling Hook", false);
					}
					
				}.runTaskLater(lobby, 20*3);
			} else {
				player.sendMessage(ChatColor.RED + "That gadget is on cooldown!");
			}
		}
		
		else if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Firework Gun")
				&& item.getType() == Material.BLAZE_ROD) {
			itemcooldown.get(player.getUniqueId().toString()).putIfAbsent(ChatColor.RED + "Firework Gun", false);
			if (!itemcooldown.get(player.getUniqueId().toString()).get(ChatColor.RED + "Firework Gun")) {
				Set<Material> NullSet = null;
				World world = player.getWorld();
				Location spawnloc = player.getTargetBlock(NullSet, 100).getLocation();
				Entity ent = world.spawnEntity(spawnloc, EntityType.FIREWORK);
				Firework fw = (Firework) ent;
				FireworkMeta meta = fw.getFireworkMeta();
				meta.addEffect(FireworkEffect.builder()
						.trail(true)
						.withColor(Color.RED)
						.withColor(Color.BLUE)
						.withColor(Color.GREEN)
						.build());
				fw.setFireworkMeta(meta);
				fw.setVelocity(new Vector(0, 1, 0));
				itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Firework Gun", true);
				new BukkitRunnable() {

					@Override
					public void run() {
						itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Firework Gun", false);
					}
					
				}.runTaskLater(lobby, 20*5);
			
			} 
			else {
				player.sendMessage(ChatColor.RED + "That gadget is on cooldown!");
			}
		} else if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Villager Launcher")
				&& item.getType() == Material.EMERALD) {
			itemcooldown.get(player.getUniqueId().toString()).putIfAbsent(ChatColor.RED + "Villager Launcher", false);
			if (!itemcooldown.get(player.getUniqueId().toString()).get(ChatColor.RED + "Villager Launcher")) {
				Entity ent = player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
				Villager villager = (Villager) ent;
				villager.setProfession(Profession.PRIEST);
				villager.setFireTicks(Integer.MAX_VALUE);
				villager.setHealth(10.0);
				villager.setVelocity(player.getLocation().getDirection().normalize().multiply(2));
				player.sendMessage(ChatColor.GREEN + "Villager coming in HOT");
				itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Villager Launcher", true);
				new BukkitRunnable() {

					@Override
					public void run() {
						itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Villager Launcher", false);
					}
					
				}.runTaskLater(lobby, 20*3);
			}
			else {
				player.sendMessage(ChatColor.RED + "That gadget is on cooldown!");
			}
		} else if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Diamond Trail")
				&& item.getType() == Material.DIAMOND_BLOCK) {
			itemcooldown.get(player.getUniqueId().toString()).putIfAbsent(ChatColor.RED + "Diamond Trail", false);
			if (!itemcooldown.get(player.getUniqueId().toString()).get(ChatColor.RED + "Diamond Trail")) {
				diamondtrailplayers.add(player);
				new BukkitRunnable() {

					@Override
					public void run() {
						diamondtrailplayers.remove(player);
					}
					
				}.runTaskLater(lobby, 20*15);
				itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Diamond Trail", true);
				new BukkitRunnable() {

					@Override
					public void run() {
						itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Diamond Trail", false);
					}
					
				}.runTaskLater(lobby, 20*45);
			}
			else {
				player.sendMessage(ChatColor.RED + "That gadget is on cooldown!");
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Player Saddle")
				&& item.getType() == Material.SADDLE) {
			itemcooldown.get(player.getUniqueId().toString()).putIfAbsent(ChatColor.RED + "Player Saddle", false);
			if (!itemcooldown.get(player.getUniqueId().toString()).get(ChatColor.RED + "Player Saddle")) {
				event.getRightClicked().setPassenger(player);
				itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Player Saddle", true);
				new BukkitRunnable() {

					@Override
					public void run() {
						itemcooldown.get(player.getUniqueId().toString()).replace(ChatColor.RED + "Player Saddle", false);
					}
					
				}.runTaskLater(lobby, 20*30);
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "That gadget is on cooldown!");
		}
	}



	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			createInv(player);
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isOnGround() && diamondtrailplayers.contains(player)) {
			Location loc = player.getLocation().subtract(0, 1, 0);
			Material type = loc.getBlock().getType();
			short data = loc.getBlock().getData();
			if (type != Material.DIAMOND_BLOCK) {
				new BukkitRunnable() {

					@Override
					public void run() {
						loc.getBlock().setType(type);
						loc.getBlock().setData((byte) data);
					}
					
				}.runTaskLater(lobby, 5*20);
				player.getLocation().subtract(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);
			}
		}
	}
	
	
}