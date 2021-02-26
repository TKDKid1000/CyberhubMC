package net.Cyberhub.tkdkid1000.resources;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.Cyberhub.tkdkid1000.CyberhubBeaconwars;
import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class HotbarItems implements Listener {

	private CyberhubBeaconwars beacon;

	public HotbarItems(CyberhubBeaconwars beacon) {
		this.beacon = beacon;
	}
	
	public void setup() {
		beacon.getServer().getPluginManager().registerEvents(this, beacon);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() == null) return;
		if (event.getItem().getType() == Material.DIAMOND
				&& event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN  + "Donate")) {
			event.setCancelled(true);
			event.getPlayer().chat("/buy");
			event.getPlayer().spigot().sendMessage(new ComponentBuilder("Click to visit our store!")
					.color(ChatColor.GOLD)
					.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cyberhubnet.tebex.io/"))
					.create());
		} else if (event.getItem().getType() == Material.BEACON
				&& event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN  + "Play")) {
			event.setCancelled(true);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "warp waiting " + event.getPlayer().getUniqueId());
		} else if (event.getItem().getType() == Material.PAPER
				&& event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN  + "Stats")) {
			event.setCancelled(true);
			event.getPlayer().chat("/stat");
		} 
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		if (!player.getWorld().getName().equalsIgnoreCase(beacon.config.getString("spawnworld"))) return;
		if (event.getClickedInventory().toString().equals(player.getInventory().toString())) {
			if (!player.hasPermission("cyberhubbeacon.override")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().getInventory().setItem(2, new ItemBuilder(Material.PAPER, 1)
				.setName(ChatColor.GREEN + "Stats")
				.build());
		event.getPlayer().getInventory().setItem(4, new ItemBuilder(Material.BEACON, 1)
				.setName(ChatColor.GREEN + "Play")
				.build());
		event.getPlayer().getInventory().setItem(6, new ItemBuilder(Material.DIAMOND, 1)
				.setName(ChatColor.GREEN + "Donate")
				.build());
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(beacon.config.getString("spawnworld"))) {
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
			event.getPlayer().getInventory().setItem(2, new ItemBuilder(Material.PAPER, 1)
					.setName(ChatColor.GREEN + "Stats")
					.build());
			event.getPlayer().getInventory().setItem(4, new ItemBuilder(Material.BEACON, 1)
					.setName(ChatColor.GREEN + "Play")
					.build());
			event.getPlayer().getInventory().setItem(6, new ItemBuilder(Material.DIAMOND, 1)
					.setName(ChatColor.GREEN + "Donate")
					.build());
		} else {
			event.getPlayer().getInventory().clear();
		}
	}
}
