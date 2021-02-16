package net.Cyberhub.tkdkid1000;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class ServerGui implements Listener {

	private CyberhubLobby cyberhublobby;
	private Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Server Switcher");
	
	public ServerGui(CyberhubLobby cyberhublobby) {
		this.cyberhublobby = cyberhublobby;
	}
	
	public void setup() {
		cyberhublobby.getServer().getMessenger().registerOutgoingPluginChannel(cyberhublobby, "BungeeCord");
		cyberhublobby.getServer().getPluginManager().registerEvents(this, cyberhublobby);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		if (event.getInventory().getName().equals(inv.getName())) {
			ItemStack clicked = event.getCurrentItem();
			event.setCancelled(true);
			if (clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Kitpvp")) {
				joinServer(player, "kitpvp");
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Beaconwars")) {
				joinServer(player, "beacon");
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Minigames")) {
				joinServer(player, "minigames");
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Event")) {
				joinServer(player, "survival");
			}
		} else if (event.getClickedInventory().toString().equals(player.getInventory().toString())) {
			if (!player.hasPermission("cyberhublobby.override")) {
				event.setCancelled(true);
			}
		}
	}
	
	public void openGui(Player player) {
		inv.clear();
		inv.setItem(1, new ItemBuilder(Material.DIAMOND_SWORD, 1)
				.setName(ChatColor.GOLD + "Kitpvp")
				.addLore(ChatColor.WHITE + "Kitpvp is a pvp based")
				.addLore(ChatColor.WHITE + "persistent game. ")
				.addLore(ChatColor.WHITE + "Kill people,")
				.addLore(ChatColor.WHITE + "get money, WIN!")
				.addLore(ChatColor.RED + "Version: 1.8-1.16")
				.addLore(ChatColor.RED + "Recommended Version: 1.8")
				.build());
		inv.setItem(3, new ItemBuilder(Material.BEACON, 1)
				.setName(ChatColor.GOLD + "Beaconwars")
				.addLore(ChatColor.WHITE + "Beaconwars is a game")
				.addLore(ChatColor.WHITE + "about protecting a beacon.")
				.addLore(ChatColor.WHITE + "You also try to destroy other")
				.addLore(ChatColor.WHITE + "people's beacons!")
				.addLore(ChatColor.WHITE + "Destroy beacon, kill team, WIN!")
				.addLore(ChatColor.RED + "Version: 1.8-1.16")
				.addLore(ChatColor.RED + "Recommended Version: 1.8")
				.build());
		inv.setItem(5, new ItemBuilder(Material.WOOL, 1)
				.setName(ChatColor.GOLD + "Minigames")
				.addLore(ChatColor.WHITE + "On minigames we")
				.addLore(ChatColor.WHITE + "have a variety of")
				.addLore(ChatColor.WHITE + "games. Like Block Sumo, or Bridging!")
				.addLore(ChatColor.RED + "Version: 1.8-1.16")
				.addLore(ChatColor.RED + "Recommended Version: 1.12")
				.build());
		inv.setItem(7, new ItemBuilder(Material.GOLDEN_APPLE, 1)
				.setName(ChatColor.GOLD + "Event")
				.addLore(ChatColor.WHITE + "Every week we host events here.")
				.addLore(ChatColor.WHITE + "You can participate in them for")
				.addLore(ChatColor.WHITE + "free, but if you win you get")
				.addLore(ChatColor.WHITE + "a free rank!")
				.addLore(ChatColor.RED + "Version: 1.16")
				.build());
		for (int x=0; x<inv.getSize(); x++) {
			if (inv.getItem(x) == null) {
				inv.setItem(x, new ItemBuilder(Material.STAINED_GLASS_PANE, 1)
						.setName(" ")
						.build());
			}
		}
		player.openInventory(inv);
	}
	
	public void joinServer(Player player, String servername) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(servername);
		player.sendPluginMessage(cyberhublobby, "BungeeCord", out.toByteArray());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() != null) {
			if (event.getItem().getType() == Material.COMPASS
					&& event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Server Switcher")) {
				openGui(event.getPlayer());
			}
		}
	}
}
