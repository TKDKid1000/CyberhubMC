package net.Cyberhub.tkdkid1000;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Cyberhub.tkdkid1000.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class Trails implements Listener, CommandExecutor {

	private Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Trails");
	private CyberhubLobby cyberhublobby;
	private HashMap<UUID, Effect> trails = new HashMap<UUID, Effect>();
	public Trails(CyberhubLobby cyberhublobby) {
		this.cyberhublobby = cyberhublobby;
	}
	
	public void register() {
		cyberhublobby.getServer().getPluginManager().registerEvents(this, cyberhublobby);
		cyberhublobby.getCommand("trails").setExecutor(this);
		new BukkitRunnable() {

			@Override
			public void run() {
				HashMap<UUID, Effect> trailsiter = trails;
				for (Map.Entry<UUID, Effect> trail : trailsiter.entrySet()) {
					Player p = Bukkit.getPlayer(trail.getKey());
					if (p != null) {
						Effect particle = trail.getValue();
						p.getWorld().playEffect(p.getLocation().add(0, 2, 0), particle, 1);
					}
				}
			}
			
		}.runTaskTimer(cyberhublobby, 1, 1);
	}

	public void createGui(Player player) {
		gui.clear();
		gui.setItem(9, new ItemBuilder(Material.BLAZE_ROD, 1)
				.setName(ChatColor.GOLD + "Flame Trail")
				.build());
		gui.setItem(11, new ItemBuilder(Material.SLIME_BALL, 1)
				.setName(ChatColor.GREEN + "Slime Trail")
				.build());
		gui.setItem(13, new ItemBuilder(Material.WATER_BUCKET, 1)
				.setName(ChatColor.BLUE + "Water Trail")
				.build());
		gui.setItem(53, new ItemBuilder(Material.BARRIER, 1)
				.setName(ChatColor.RED + "No Trail")
				.build());
		gui.setItem(49, new ItemBuilder(Material.REDSTONE, 1)
				.setName(ChatColor.RED + "Back to Gadgets")
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
		if (!event.getInventory().getName().equals(gui.getName())) return;
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() == null) return;
		ItemStack clicked = event.getCurrentItem();
		event.setCancelled(true);
		if (clicked.getType() == Material.GLASS) return;
		if (clicked.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Flame Trail")
				&& clicked.getType() == Material.BLAZE_ROD) {
			if (trails.containsKey(player.getUniqueId())) {
				trails.replace(player.getUniqueId(), Effect.FLAME);
			} else {
				trails.put(player.getUniqueId(), Effect.FLAME);
			}
			player.sendMessage(ChatColor.GREEN + "Set your trail to flame!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Slime Trail")
				&& clicked.getType() == Material.SLIME_BALL) {
			if (trails.containsKey(player.getUniqueId())) {
				trails.replace(player.getUniqueId(), Effect.SLIME);
			} else {
				trails.put(player.getUniqueId(), Effect.SLIME);
			}
			player.sendMessage(ChatColor.GREEN + "Set your trail to slime!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Water Trail")
				&& clicked.getType() == Material.WATER_BUCKET) {
			if (trails.containsKey(player.getUniqueId())) {
				trails.replace(player.getUniqueId(), Effect.WATERDRIP);
			} else {
				trails.put(player.getUniqueId(), Effect.WATERDRIP);
			}
			player.sendMessage(ChatColor.GREEN + "Set your trail to water!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "No Trail")
				&& clicked.getType() == Material.BARRIER) {
			if (trails.containsKey(player.getUniqueId())) {
				trails.remove(player.getUniqueId());
			}
			player.sendMessage(ChatColor.GREEN + "Cleared your trail!");
		} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "Back to Gadgets")
				&& clicked.getType() == Material.REDSTONE) {
			player.performCommand("gadget");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			createGui(player);
		}
		return true;
	}
}
